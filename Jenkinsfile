pipeline
    {
       agent {
            docker {
                image 'default-route-openshift-image-registry.apps.sandbox-m2.ll9k.p1.openshiftapps.com/stevencurtis-dev/openjdk17'
                args '--privileged'  // Optional: Use this if required by your environment
            }
        }

        stages
        {
          stage('Install JDK 17') {
            steps {
                script {
                    // Download and install JDK 17
                    sh '''
                    wget https://download.java.net/java/17/latest/jdk-17_linux-x64_bin.tar.gz
                    tar -xzf jdk-17_linux-x64_bin.tar.gz
                    export JAVA_HOME=$(pwd)/jdk-17
                    export PATH=$JAVA_HOME/bin:$PATH
                    echo "JAVA_HOME=$JAVA_HOME"
                    echo "PATH=$PATH"
                    java -version
                    '''
                }
            }
          }
          stage('Build App')
          {
            steps {
                // Ensure JAVA_HOME and PATH are set for this stage
                withEnv(["JAVA_HOME=${pwd()}/jdk-17", "PATH=${pwd()}/jdk-17/bin:${env.PATH}"]) {
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
                    openshift.newBuild("--name=sample-app-jenkins-new", "--image-stream=openjdk18-openshift:1.14-3", "--binary=true")
                  }
                }
              }
            }
          }
          stage('Build Image') {
            steps {
              sh "rm -rf ocp && mkdir -p ocp/deployments"
              sh "pwd && ls -la target "
              sh "cp target/openshiftjenkins-0.0.1-SNAPSHOT.jar ocp/deployments"

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
