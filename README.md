# Pluggable Annotation Processing API Examples
This is an example project for Pluggable Annotation Processing API (JSR 269).

## How to run

$ cd /<path-to-project>

# Install library
$ cd annotation-processor
$ mvn clean install


# Do Annotation Processing

Add below maven dependency
		<dependency>
            <groupId>com.citi.cpb.perf.annotation</groupId>
            <artifactId>annotation-processor</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>


@StartTimer  - Apply annotation on method, this will start timer. (Starting method)
@AddTimer    - Apply annotation on method, this will add timer.

