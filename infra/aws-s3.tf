resource "aws_s3_bucket" "assets" {
  bucket = "bfi-iiif-assets-${terraform.workspace}"
  acl    = "private"

  tags = merge(
    local.common_tags,
    {
      "Name" = "bfi-iiif-assets-${terraform.workspace}"
    }
  )
}

resource "aws_s3_bucket_object" "assets_images_bfi-147x150-png" {
  bucket = aws_s3_bucket.assets.bucket
  key    = "images/bfi-147x150.png"
  source = "${path.module}/assets/images/bfi-147x150.png"
  etag   = filemd5("${path.module}/assets/images/bfi-147x150.png")
  acl    = "public-read"

  tags = merge(
    local.common_tags,
    {
      "Name" = "images/bfi-147x150.png"
    }
  )
}
