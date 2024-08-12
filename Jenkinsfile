pipeline
    {
       agent {
            label 'maven'
        }

        stages
        {
          stage('Build App') {
            steps {
                script {
                    // Pull Docker image with Java 17
                    sh '''
                    docker pull default-route-openshift-image-registry.apps.sandbox-m2.ll9k.p1.openshiftapps.com/stevencurtis-dev/openjdk17
                    '''

                    // Run Maven build inside Docker container
                    sh '''
                    docker run --rm \
                        -v $WORKSPACE:/workspace \
                        -w /workspace \
                        default-route-openshift-image-registry.apps.sandbox-m2.ll9k.p1.openshiftapps.com/stevencurtis-dev/openjdk17 \
                        mvn clean install
                    '''
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
