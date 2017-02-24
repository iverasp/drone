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

    brew install homebrew/science/opencv --with-java    
    cp /usr/local/Cellar/opencv/2.4.13.1/share/OpenCV/java/*.dylib /Library/Java/Extensions

The library will be produced at

    /usr/local/Cellar/opencv/2.4.13.1/share/OpenCV/java/opencv-2413.jar

## Set permissions for I2C, SPI, UART

Create groups and add your user to those groups

    sudo groupadd -f --system spi
    sudo groupadd -f --system i2c
    sudo adduser <user> spi
    sudo adduser <user> i2c

Edit __/etc/udev/rules.d/90-spi.rules__ and add

    SUBSYSTEM=="spidev", GROUP="spi"

Edit __/etc/udev/rules.d/91-i2c.rules__ and add

    SUBSYSTEM=="i2c-dev", GROUP="i2c"
