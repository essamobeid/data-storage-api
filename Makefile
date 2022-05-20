all: build

build:
	./gradlew clean build test

run: build
	./gradlew bootRun

test: build
	./gradlew test
	
clean:
	./gradlew clean
