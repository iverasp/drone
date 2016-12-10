## Install Oracle Java SDK 8

    apt-get install oracle-java8-jdk

## Install Pi4J

    curl -s get.pi4j.com | bash

## (Raspbian) Compile and install OpenCV

    git clone git://github.com/opencv/opencv.git
    cd opencv
    git checkout 2.4
    mkdir build
    cd build

Check if JAVA_HOME is set correctly

    echo $JAVA_HOME

Set JAVA_HOME if not set correctly

    export JAVA_HOME="$(/usr/libexec/java_home -v 1.8)"

Generate Makefile and compile OpenCV

    cmake -DBUILD_SHARED_LIBS=OFF ..
    make -j4

## (macOS) Compile and install OpenCV with Homebrew

    brew install opencv --with-java
