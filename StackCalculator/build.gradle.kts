plugins {
    id("java")
    id("application")  // Для запуска приложения через gradle run
}

group = "calculator"  // Замените на ваш package, если отличается
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Для утилит (опционально)
    implementation("org.apache.commons:commons-lang3:3.13.0")
}

application {
    mainClass.set("calculator.Calculator")  // Указываем ваш главный класс
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))  // Используем Java 17
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"  // Кодировка исходных файлов
    }

    // Создание fat-JAR (со всеми зависимостями)
    register<Jar>("fatJar") {
        archiveClassifier.set("all")
        manifest {
            attributes["Main-Class"] = application.mainClass.get()
        }
        from(sourceSets.main.get().output)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map { zipTree(it) }
        })
    }
}