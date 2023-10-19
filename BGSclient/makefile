CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread


all: bin/BGSclient


bin/BGSclient: bin/connectionHandler.o bin/BGSclient.o bin/connectionReader.o bin/keyboardReader.o
	@echo 'Building target: BGSclient'
	@echo 'Invoking: C++ Linker'
	g++ -Wall -Weffc++ -o bin/BGSclient bin/connectionHandler.o bin/BGSclient.o bin/connectionReader.o bin/keyboardReader.o $(LDFLAGS)
	@echo 'Finished building target: BGSclient'

bin/BGSclient.o: src/BGSclient.cpp
	g++ $(CFLAGS) -o bin/BGSclient.o src/BGSclient.cpp
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp
bin/connectionReader.o: src/connectionReader.cpp
	g++ $(CFLAGS) -o bin/connectionReader.o src/connectionReader.cpp
bin/keyboardReader.o: src/keyboardReader.cpp
	g++ $(CFLAGS) -o bin/keyboardReader.o src/keyboardReader.cpp

clean:
	rm -f bin/*