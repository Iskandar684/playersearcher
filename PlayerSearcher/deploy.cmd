docker build -t player-searcher:latest .
docker stop player-searcher
docker rm player-searcher
docker run -d -p 8080:8080 --name player-searcher player-searcher:latest
