package main.tests

import org.apache.catalina.core.ApplicationContext

class Script {
    static void main(String[] args) {
        println ">>> Script was stared"
        ApplicationContext.getClasses().each { println it }
    }
}
