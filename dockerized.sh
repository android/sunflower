#!/usr/bin/env bash

echo "Usage: dockerized.sh '<command>' [<id>]"
id=123
if [[ "$#" -eq 2 ]]; then
    id=$2
fi

container_name=box-${id}
home_dir=/home/user
project_dir=${home_dir}/project
echo "Check for container $container_name"
docker ps | grep ${container_name}

if [[ "$?" -eq 1 ]]; then
	echo "No docker container, run it"
	docker rm ${container_name}
	docker run -d -it \
		-v $HOME/.gradle:${home_dir}/.gradle\
		-v $HOME/.android:${home_dir}/.android\
		-v $(pwd):${project_dir}\
		-w ${project_dir}\
		--name ${container_name} vbratashchuk/docker-android bash
fi

echo "Use ANDROID_HOME env instead of local.properties"
docker exec -i ${container_name} rm local.properties
docker exec -i ${container_name} $1
result=$?

docker stop ${container_name}

exit ${result}
