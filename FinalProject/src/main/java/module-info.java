
module com.mycompany.finalproject.FinalProject {
    requires com.pi4j;
    requires com.pi4j.plugin.raspberrypi;
    requires com.pi4j.plugin.pigpio;
    requires com.pi4j.library.pigpio;
    requires org.slf4j;
    requires org.slf4j.simple;
    requires org.apache.commons.io;
    requires transitive javafx.controls;
    requires java.logging;
    requires transitive eu.hansolo.tilesfx;
    requires transitive com.hivemq.client.mqtt;
    exports com.mycompany.finalproject;
    uses com.pi4j.extension.Extension;
    uses com.pi4j.provider.Provider;
}
