package com.justthor

import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.security.AWSCredentials
import org.jets3t.service.model.S3Object
import org.jets3t.service.acl.{ AccessControlList, GroupGrantee, Permission }
import java.io.InputStream

object Main extends App{
  val classPath = "/"
    
  // upload a simple text file
  val textFilename = "test.txt"
  val linkForText = store(textFilename, getClass.getResourceAsStream(s"$classPath$textFilename"))
  
  // upload a cat image, taken from http://imgur.com/gallery/bTiwg
  // set the content type to "image/jpg"
  val imageFilename = "cat.jpg"
  val linkForImage = store(imageFilename, getClass.getResourceAsStream(s"$classPath$imageFilename"), "image/jpg")
  
  println(s"Url for the text file is $linkForText")
  println(s"Url for the cat image is $linkForImage")
  
  
  def store(key: String, inputStream: InputStream, contentType: String = "text/plain") = {
    val awsAccessKey = "YOUR_ACCESS_KEY"
    val awsSecretKey = "YOUR_SECRET_KEY"
    val awsCredentials = new AWSCredentials(awsAccessKey, awsSecretKey)
    val s3Service = new RestS3Service(awsCredentials)
    val bucketName = "test-scala-upload"
    val bucket = s3Service.getOrCreateBucket(bucketName)
    val fileObject = s3Service.putObject(bucket, {
      // public access is disabled by default, so we have to manually set the permission to allow read access to the uploaded file
      val acl = s3Service.getBucketAcl(bucket)
      acl.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ)
      
      val tempObj = new S3Object(key)
      tempObj.setDataInputStream(inputStream)
      tempObj.setAcl(acl)
      tempObj.setContentType(contentType)
      tempObj
    })

    s3Service.createUnsignedObjectUrl(bucketName,
      fileObject.getKey,
      false, false, false)
  }
}