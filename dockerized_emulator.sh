#!/usr/bin/env bash

echo "Usage: dockerized_emulator.sh '<command>' [<port>]"
port=5555
if [[ "$#" -eq 2 ]]; then
    port=$2
fi

container_name=box-${port}
home_dir=/home/user
project_dir=${home_dir}/project
echo "Check for container $container_name"
docker ps | grep ${container_name}

if [[ "$?" -eq 1 ]]; then
	echo "No docker container, run it"
	docker rm ${container_name}
	docker run -d -it -p ${port}:${port}\
		--expose ${port}\
		-v $HOME/.gradle:${home_dir}/.gradle\
		-v $HOME/.android:${home_dir}/.android\
		-v $(pwd):${project_dir}\
		-w ${project_dir}\
		--name ${container_name} vbratashchuk/docker-android bash
fi

echo "Connect to emulator on the host and make it use ANDROID_HOME env instead of local.properties"
docker exec -i ${container_name} bash -c "adb connect host.docker.internal:${port}; rm local.properties"
docker exec -i ${container_name} $1
result=$?

docker stop ${container_name}

exit ${result}
