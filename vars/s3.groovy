def login(){
    aws.login()
}

def upload(bucket, from, to=null){
    login()
    if(!to){
        to = from
    }
    sh "aws s3 cp ${from} s3://${bucket}/${to}"
}

def download(bucket, from, to=null){
    login()
    if(!to){
        to = from
    }
    sh "aws s3 cp s3://${bucket}/${from} ${to}"
}

def upload_folder(bucket, from, to=null){
    login()
    if(!to){
        to = from
    }
    sh "aws s3 sync ${from} s3://${bucket}/${to}"
}

def download_folder(bucket, from, to=null){
    login()
    if(!to){
        to = from
    }
    sh "aws s3 sync s3://${bucket}/${from} ${to}"
}
