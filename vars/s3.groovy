def upload(bucket, from, to=null){
    to = to || from
    withAWS(credentials: "${AWS_KEY}") {
		s3Upload(file: from, bucket: bucket, path: to)
    }
}

def download(bucket, from, to=null){
    to = to || from
    withAWS(credentials: "${AWS_KEY}") {
		s3Download(file: to, bucket: bucket, path: path)
    }
}
