package main.tests

import org.apache.catalina.core.ApplicationContext

println ">>> Script was stared"
ApplicationContext.getClasses().each { println it }

