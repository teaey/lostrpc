@echo off

set path=%path%;../../../../lostrpc.resources/

protoc --java_out=../java/ *.proto

@pause