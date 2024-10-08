pipeline
    {
        agent {
            label 'maven'
        }
        stages
        {
          stage('Build App')
          {
            steps {
                git branch: 'main', url: 'https://github.com/StevenSMC8/spring-jenkins-test.git'
                sh 'echo $JAVA_HOME'
                sh 'echo $PATH'
                sh 'java -version'
                sh 'mvn -version'
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    version = pom.version
                }
                sh "mvn install"
            }
          }
          stage('Create Image Builder') {
            when {
              expression {
                openshift.withCluster() {
                  openshift.withProject() {
                    return !openshift.selector("bc", "sample-app-jenkins-new").exists();
                  }
                }
              }
            }
            steps {
              script {
                openshift.withCluster() {
                  openshift.withProject() {
                    openshift.newBuild("--name=sample-app-jenkins-new", "--image-stream=openjdk-11:1.20-2.1721172731", "--binary=true", "--strategy=source")
                  }
                }
              }
            }
          }
          stage('Build Image') {
            steps {
              sh "rm -rf ocp && mkdir -p ocp/deployments"
              sh "pwd && ls -la target "
              sh "cp target/spring-boot-thymeleaf-0.0.1-SNAPSHOT.jar ocp/deployments"

              script {
                openshift.withCluster() {
                  openshift.withProject() {
                    openshift.selector("bc", "sample-app-jenkins-new").startBuild("--from-dir=./ocp","--follow", "--wait=true")
                  }
                }
              }
            }
          }
          stage('deploy') {
            when {
              expression {
                openshift.withCluster() {
                  openshift.withProject() {
                    return !openshift.selector('dc', 'sample-app-jenkins-new').exists()
                  }
                }
              }
            }
            steps {
              script {
                openshift.withCluster() {
                  openshift.withProject() {
                    def app = openshift.newApp("sample-app-jenkins-new", "--as-deployment-config")
                    app.narrow("svc").expose();
                  }
                }
              }
            }
          }
        }
    }
