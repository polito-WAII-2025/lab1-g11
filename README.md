[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/vlo9idtn)
# lab1-wa2025


## Build the Docker Image

Build the Docker image for the route analyzer:

```sh
docker build -t route-analyzer ./RouteAnalyzer
```

## Run the Container

Use the following commands to run the container with different configurations:

- **Default Files**:  
  Mount the `evaluation` directory with the default files:

  ```sh
  docker run -p 8080:8080 -v "$(PWD)/evaluation:/app/evaluation" route-analyzer
  ```

- **Custom Parameters & Waypoints**:  
  Run with custom parameters (`--customparams`) and/or waypoints (`--waypoints`):

  ```sh
  docker run -p 8080:8080 -v "$(PWD)/evaluation:/app/evaluation" route-analyzer \
    --waypoints "/app/evaluation/waypoints.csv" \
    --customparams "/app/evaluation/custom-parameters.yml"

  ```

  You can use either or both flags:
    - `--customparams` to specify custom parameters.
    - `--waypoints` to specify waypoints.