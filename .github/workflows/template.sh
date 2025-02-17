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

#레디스 설치
sudo apt-get install lsb-release curl gpg
curl -fsSL https://packages.redis.io/gpg | sudo gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg
sudo chmod 644 /usr/share/keyrings/redis-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/redis.list
sudo apt-get update
sudo apt-get install redis

#도커 자동실행 설정
sudo systemctl start docker
sudo systemctl enable docker

#sudo 도커 권한 부여
sudo usermod -aG docker ubuntu

#도커컴포즈 설치
DOCKER_COMPOSE_VERSION=$(curl -s https://api.github.com/repos/docker/compose/releases/latest | grep '"tag_name":' | sed -E 's/.*"v([^"]+)".*/\1/')
sudo curl -L "https://github.com/docker/compose/releases/download/v${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

#도커 로그인
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

#cheerha 디렉토리 생성
mkdir -p ~/cheer-ha

#환경변수 파일 생성
echo "$ENV_FILE" > ~/cheer-ha/.env

#도커컴포즈 파일 생성
cat <<EOF > /home/ubuntu/cheer-ha/docker-compose-release.yml
services:
  app:
    image: lcyoun9/cheer-ha:latest
    container_name: cheer-ha-app
    restart: always
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    env_file: .env

  mysql:
    image: mysql:8.0
    container_name: \${DB_URL}
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: \${ROOT_PASS}
      MYSQL_DATABASE: \${DB_NAME}
      MYSQL_USER: \${DB_USER}
      MYSQL_PASSWORD: \${DB_PASSWORD}
    ports:
      - "\${DB_PORT}:3306"
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:

EOF

#컨테이너 실행
docker-compose -f ~/cheer-ha/docker-compose-release.yml down
docker pull lcyoun9/cheer-ha:latest
docker-compose -f ~/cheer-ha/docker-compose-release.yml up -d --force-recreate