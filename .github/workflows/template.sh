#!/bin/bash
#패키지 목록 업데이트 & 필수 패키지 설치
sudo apt update -y
sudo apt install -y ca-certificates curl gnupg lsb-release

#도커 설치
sudo mkdir -m 0755 -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo tee /etc/apt/keyrings/docker.asc > /dev/null
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update -y
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

#도커 자동실행 설정
sudo systemctl start docker
sudo systemctl enable docker

#도커컴포즈 설치
DOCKER_COMPOSE_VERSION=$(curl -s https://api.github.com/repos/docker/compose/releases/latest | grep '"tag_name":' | sed -E 's/.*"v([^"]+)".*/\1/')
sudo curl -L "https://github.com/docker/compose/releases/download/v${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

#cheerha 디렉토리 생성
mkdir -p ~/cheer-ha

#환경변수 파일 생성
cat <<EOF > /home/ubuntu/cheer-ha/.env
#env 붙여넣기
EOF

#도커컴포즈 파일 생성
cat <<EOF > /home/ubuntu/cheer-ha/docker-compose-release.yml
#컴포즈 붙여넣기
EOF

#컨테이너 실행
sudo docker-compose -f ~/cheer-ha/docker-compose-release.yml down
sudo docker pull lcyoun9/cheer-ha:latest
sudo docker-compose -f ~/cheer-ha/docker-compose-release.yml up -d --force-recreate