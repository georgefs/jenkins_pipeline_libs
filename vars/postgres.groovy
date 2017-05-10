def call(body){
    def user = "root"
    def password = "test"
    def container = sh(returnStdout: true, script: "docker run -d --rm -p 5432 -e POSTGRES_USER=${user} -e POSTGRES_PASSWORD=${password} postgres").trim()
    def host = sh(returnStdout: true, script: "docker inspect --format '{{ .NetworkSettings.IPAddress }}' ${container}").trim()
    def port = "5432"
    def db = "db"
    withEnv(["PGHOST=${host}", "PGUSER=${user}", "PGPASSWORD=${password}", "PGPORT=${port}", "POSTGRES_DB=${db}"]){
        body()
    }
    sh "docker rm -f ${container}"
}
