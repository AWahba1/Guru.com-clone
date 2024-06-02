# Guru.com-Clone: A Scalable Microservices Application Replicated From Guru

![SpringBoot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![ApacheCassandra](https://img.shields.io/badge/cassandra-%231287B1.svg?style=for-the-badge&logo=apache-cassandra&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Firebase](https://img.shields.io/badge/firebase-FFA611.svg?style=for-the-badge&logo=firebase&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-%23FF6600.svg?style=for-the-badge&amp;logo=rabbitmq&amp;logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=Kubernetes&logoColor=white)
![Nginx](https://img.shields.io/badge/nginx-009900.svg?style=for-the-badge&logo=nginx&logoColor=white)
![Prometheus](https://img.shields.io/badge/Prometheus-000000?style=for-the-badge&logo=prometheus&labelColor=000000)
![Grafana](https://img.shields.io/badge/Grafana-000000?style=for-the-badge&logo=grafana&labelColor=000000)
![GithubActions](https://img.shields.io/badge/Github%20Actions-282a2e?style=for-the-badge&logo=githubactions&logoColor=367cfe)


## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture](#%EF%B8%8F-architecture)
3. [Implementation](#%EF%B8%8F-implementation)
   - [Databases](#%EF%B8%8F-databases)
     - [PostgreSQL](#postgresql)
     - [Apache Cassandra](#cassandra)
     - [Firebase](#firebase)
     - [Redis](#redis)
   - [Microservices](#%EF%B8%8F-microservices)
     - [Users Microservice](#users-microservice)
     - [Freelancers Microservice](#freelancers-microservice)
     - [Jobs Microservice](#jobs-microservice)
     - [Messaging Microservice](#messaging-microservice)
     - [Notifications Microservice](#notifications-microservice)
     - [Reports & Feedbacks Microservice](#reports-microservice)
     - [Payments Microservice](#payments-microservice)
   - [Media Server](#-media-server)
   - [Message Queues](#-message-queues)
   - [Dockerizing The App](#-dockerizing-the-app)
     - [Cleaning & Packaging Microservices](#cleaning) 
     - [Building Docker Images](#building-docker-images)
     - [Uploading Images on DockerHub](#uploading-images-dockerhub)
   - [Deployment](#-deployment)
     - [Kubernetes](#kubernetes)
     - [Auto Scalar](#auto-scalar)
     - [Service Discovery](#service-discovery)
     - [Load Balancer](#load-balancer)
     - [Web Server](#web-server)
     - [CI/CD](#continous-integration)
   - [Logging & Monitoring](#-logging--monitoring)
     - [SLF4J](#slf4j)
     - [Prometheus](#prometheus)
     - [Grafana](#grafana)
4. [API Documentation](#-api-documentation)
5. [Load Testing](#-load-testing)


## Project Overview

Guru.com-Clone is a microservices-driven platform enabling freelancers and clients to engage, collaborate, and accomplish projects, mirroring the functionalities of Guru. 💼 Engineered with scalability and resilience in focus, the system leverages a variety of technologies to adeptly manage high volumes of traffic and extensive data. 🚀

## 🏛️ Architecture

<!-- ![guru-clone_arch]() -->


## 🛠️ Implementation

### 🛢️ Databases
 The system utilizes a variety of databases tailored to specific needs:

- PostgreSQL: Employs a robust relational centeralized database for structured data storage and management.
- Apache Cassandra: Utilizes a scalable and distributed NoSQL database for handling large volumes of unstructured data with high availability and fault tolerance.
- Firebase: Integrates a cloud-based database solution.
- Redis: Implements an in-memory data store for caching frequently accessed data, optimizing response times and reducing computational overhead.
  
<details>
   <summary id="postgresql">
      🐘 PostgreSQL
   </summary>
  In the system architecture, a centralized PostgreSQL database is employed as the main data repository accessed by most microservices. Notably, due to the read-intensive nature of these services and the complexity of their queries, they extensively rely on the centralized PostgreSQL database. This centralized approach facilitates efficient data management and accessibility across the system, accommodating the intricate relationships between different data entities and supporting the execution of complex queries tailored to the specific requirements of each microservice.
</details>

<details>
   <summary id="cassandra">
      👁 Apache Cassandra
   </summary>
  The message and report/feedback services in the system leverage Apache Cassandra as their underlying data store, owing to their write-intensive operations and straightforward CRUD (Create, Read, Update, Delete) queries. Apache Cassandra's distributed architecture and support for linear scalability make it well-suited for accommodating the high volume of write operations generated by these services. Additionally, the simplicity of their data relationships and query patterns eliminates the need for complex table replication based on different keys, streamlining data management and ensuring efficient performance.
</details>

<details>
   <summary id="firebase">
      🔥 Firebase
   </summary>
  Firebase serves as the media server for the system, providing a cloud-based solution for storing and serving media content. Its real-time database and storage capabilities enable seamless integration with the application, allowing users to upload, retrieve, and stream media files with minimal latency. Firebase's scalability and reliability ensure uninterrupted access to media content, while its authentication and security features safeguard sensitive data. By leveraging Firebase as a media server, the system delivers a seamless and responsive multimedia experience to users across platforms.
</details>

<details>
   <summary id="redis">
      💾 Redis
   </summary>
  Redis plays a pivotal role in the system architecture, serving as a high-performance caching layer for optimizing data access and response times. Utilized across various microservices, Redis efficiently stores and retrieves frequently accessed data, such as session information, user preferences, and temporary application state. Its in-memory data storage and support for data structures enable fast and reliable caching, reducing the need for repeated computations and database queries. By leveraging Redis, the system enhances scalability, resilience, and overall performance, ensuring a seamless and responsive user experience. This caching mechanism significantly reduces the load on the database server, alleviating potential bottlenecks and enhancing overall system performance by minimizing the need for repetitive and resource-intensive database queries.
</details>

### 🏗️ Microservices

- All microservices are implemented using Java Spring Boot ☕.
- They consume messages from the RabbitMQ message broker 🐰 and respond through RabbitMQ as well.
- Requests are cached in Redis, so if the same request is sent more than once, there is no need to recompute the response every time and fetch the data again from the database as it can be retrieved directly from the cache 🗃️.
- In some cases, a microservice would have to communicate with another one to complete a certain functionality, which is done through RabbitMQ. Every microservice can be scaled up or down independently of other microservices to adapt to the amount of incoming traffic.

<details>
   <summary id="users-microservice">
      👥 Users Microservice
   </summary>
 The Users Microservice primarily focuses on user authentication (Login & Registeration), implemented using Spring Boot Security. This service interacts mainly with PostgreSQL for user data storage and management. Additionally, to optimize authentication performance, the service caches the generated JWT tokens upon successful login in a shared Redis cache. Moreover, the Users Microservice handles common operations shared by all users (clients and freelancers), such as changing username/password and other user-related functionalities.
</details>

<details>
   <summary id="freelancers-microservice">
      👨🏻‍💻 Freelancers Microservice
   </summary>
  The Freelancer Microservice serves as the backbone for managing freelancer-related operations within the Guru.com replica. Leveraging Spring Boot, this microservice facilitates freelamcer profile management, and skillset showcasing of freelancers like portfolios,services and dedicated resources.Additionally, the microservice handles tasks such as adding and removing team members(non-individual accounts) ensuring seamless collaboration between freelancers and their teams. Utilizing Redis, freelancer microservice caches most viewed freelancer profiles.
</details>

<details>
   <summary id="jobs-microservice">
       🔧 Jobs Microservice
   </summary>
  The Jobs Microservice forms the core of job management functionality within the Guru.com replica platform. Powered by Spring Boot, it enables clients to post job listings, specify project requirements, inviting freelancers to his job, and manage job postings. Moreover, it facilitates job discovery for freelancers, allowing them to browse, search, apply for, and manage active job opportunities efficiently. Additionally, the microservice handles freelancer placing quotes on different jobs, saving quote templates, adding certain jobs to jobwatchlist. Leverageing Redis, job microservice caches top frequent job search queries.
</details>

<details>
   <summary id="messaging-microservice">
       💬 Messaging Microservice
   </summary>
  The Messaging Microservice plays a pivotal role in facilitating real-time communication between clients and freelancers on the Guru.com replica platform. Utilizing WebSocket technology and Spring Boot, this microservice enables instant messaging, and collaboration within project workspaces. Leveraging Apache Cassandra as its underlying data store, it ensures scalability and reliability for handling high volumes of messaging traffic.
</details>

<details>
   <summary id="notifications-microservice">
       🔔 Notifications Microservice
   </summary>
  The Notification Microservice delivers timely and relevant notifications to users across the Guru.com replica platform. Powered by Spring Boot, it orchestrates the delivery of notifications related to quote updates(placing quote on a job posted by a client / accepting quote placed on a job by freelancer),received job invitations, profile views, and new received messaged.
</details>

<details>
   <summary id="reports-microservice">
       📝 Reports & Feedbacks Microservice
   </summary>
  The Report & Feedback Microservice is instrumental in fostering transparency and accountability within the Guru.com replica platform. Leveraging Spring Boot, this microservice empowers users to submit reports, provide feedback, and rate project engagements. Unlike other microservices, it utilizes Apache Cassandra along with messaging microservice as its underlying database solution. This choice ensures seamless scalability and fault tolerance, enabling the storage of large volumes of feedback data efficiently. With Cassandra's distributed architecture, the microservice maintains high availability and reliability, facilitating the storage and retrieval of user-generated content. 
</details>

<details>
   <summary id="payments-microservice">
      💳 Payments Microservice
   </summary>
   This microservice handles payment-related requests 💳. Both freelancers and clients have wallets, which they can add or withdraw money from. Both of them have a history of transactions, and freelancers can issue payment requests that are then paid by the clients, and the money is deposited into the freelancer's wallet. The DB used for this service is PostgreSQL 🐘, as payments require lots of ACID transactions in addition to strict consistency, which is achieved by relational databases.
</details>


 ### 🎬 Media Server

The Media Server, powered by Firebase, serves as the cornerstone for handling multimedia content within the Guru.com replica platform. Leveraging Firebase's cloud-based storage and real-time database capabilities, this server facilitates seamless uploading, storage, and retrieval of media files, including images, videos, and documents. With Firebase's robust security features and scalability, the Media Server ensures secure and reliable access to multimedia content across devices and platforms. 

### 📬 Message Queues

RabbitMQ serves as the backbone for asynchronous communication between various microservices in this project 🐰. The message queues play a pivotal role in facilitating communication, particularly for the Notification Service. This service consumes messages from different sources, including the Freelancer, Job, and Messaging Microservices, each with its dedicated queue. These queues handle a myriad of notifications, such as viewing profiles, job invitations, placing quotes, accepting quotes, and receiving new messages. This structured approach ensures timely and reliable delivery of notifications, enhancing user engagement and platform functionality.

### 🐋 Dockerizing The App

<details>
   <summary id="cleaning">
      📦 Cleaning & Packaging Microservices
   </summary>
  To Dockerize the application, the first step involved executing Maven commands to clean and package the microservices. This process entailed running mvn clean to remove any previously compiled artifacts and mvn package to compile the source code, run tests, and package the application into executable .jar files. Each microservice, structured as a Maven project, underwent this meticulous process to ensure that all dependencies were resolved and included in the packaged artifacts. These .jar files served as the executable units encapsulating the microservices, ready for containerization within Docker. This methodical approach laid a solid foundation for seamless integration and deployment within Docker containers.
</details>

<details>
   <summary id="building-docker-images">
      🔨 Building Docker Images
   </summary>
  After cleaning and packaging the microservices, the next step involved building Docker images for each microservice. This process was accomplished using Dockerfile configurations, which specified the environment and dependencies required to run the microservice within a Docker container. Leveraging Docker's build capabilities, the Docker images were created with efficiency and consistency. Each Docker image encapsulated the packaged microservice artifact, ensuring that it could be executed within a containerized environment. This step ensured that the microservices were properly containerized and ready for deployment across various environments.
</details>

<details>
   <summary id="uploading-images-dockerhub">
      📤 Uploading Images on DockerHub
   </summary>
  Once the Docker images for the microservices were built, the final step involved uploading these images to DockerHub. DockerHub served as the central repository for storing and sharing Docker images, providing a convenient platform for managing and distributing containerized applications. Each Docker image was tagged with version information and securely uploaded to DockerHub. This process made the Docker images accessible online and facilitated seamless deployment across different environments. By leveraging DockerHub, it was ensured that the Docker images were readily available for deployment, streamlining the sharing and collaboration of containerized applications.
</details>

### 🚀 Deployment
In this project, the microservices were deployed locally on a Docker Kubernetes cluster along with Postgres, PgAdmin, RabbitMQ, Redis, Prometheus, and Grafana, enabling efficient management and scalability of the application components. Leveraging Docker containers orchestrated by Kubernetes, each microservice was encapsulated and deployed as a scalable and isolated unit. This deployment approach facilitated seamless integration and testing of the microservices in a controlled environment, allowing for rapid development iterations and ensuring consistency across deployments. Additionally, the use of Kubernetes provided automated deployment, scaling, and management capabilities, empowering the team to efficiently manage and orchestrate the deployment lifecycle of the microservices. Overall, deploying the microservices locally on a Docker Kubernetes cluster enhanced development agility and reliability, laying a solid foundation for future scalability and production deployment.

<details>
   <summary id="kubernetes">
       ☸ Kubernetes
   </summary>
  The Kubernetes deployment configuration outlines the deployment details for the microservices within the application ecosystem. With a starting replica count of 2, Kubernetes ensures high availability by maintaining two instances of the microservice to handle incoming requests. Pods are selected based on the specified label, ensuring consistency in pod selection and management across the cluster. Each pod is based on a Docker image, configured to expose its own port for incoming traffic. The deployment incorporates a readiness probe, configured to check the "/actuator/health" endpoint of the Spring Boot application (That endpoint is predefined if the app uses spring boot actuator). This probe ensures that the deployed service is fully initialized and ready to accept requests before being added to the load balancer rotation. With an initial delay of 30 seconds and subsequent checks every 10 seconds, Kubernetes waits for the service to become ready before directing traffic to it. This approach prevents premature routing of requests to the service, guaranteeing a seamless user experience once the service is fully operational. Additionally, the deployment specifies resource requests and limits to manage the memory and CPU utilization of the deployed pods effectively. Resource requests are set to 128Mi of memory and 250m of CPU, while resource limits are set to 512Mi of memory and 750m of CPU. By defining these resource constraints, Kubernetes ensures efficient resource utilization and prevents resource contention among pods within the cluster.
</details>

<details>
   <summary id="auto-scalar">
       📈 Auto Scalar
   </summary>
 Before configuring the Horizontal Pod Autoscaler (HPA) for our microservices within the application ecosystem, we ensured the Kubernetes Metrics Server was enabled to gather resource utilization metrics across the cluster. With this prerequisite in place, The Horizontal Pod Autoscaler (HPA) configuration outlines the scaling behavior for a microservice within the application ecosystem. With a scale target reference to the corresponding Deployment, the HPA ensures dynamic scaling based on resource utilization metrics. The HPA is configured to scale up or down ⬆️⬇️ based on average CPU utilization, targeting a utilization threshold of 80% and scaling up to 5 parallel pod in total (limited to only 5 due to the host machine capabilites and RAM). When resource utilization exceeds this threshold, the HPA initiates scaling actions ⬆️ to increase the number of replicas, ensuring optimal performance and resource utilization. To prevent excessive scaling, the HPA incorporates scaling policies with stabilization windows for both scaling up and scaling down. These policies aim to stabilize the system before initiating scaling actions, avoiding rapid fluctuations in replica counts and ensuring stability under varying workload conditions. Overall, this HPA configuration enables adaptive scaling of microservices based on resource utilization, enhancing efficiency and performance within the Kubernetes cluster.
</details>

<details>
   <summary id="service-discovery">
       🔎 Service Discovery
   </summary>
  In Kubernetes, service discovery is a crucial aspect of managing distributed applications. Kubernetes provides built-in service discovery mechanisms that allow applications to locate and communicate with each other dynamically. This is achieved through Kubernetes Services, which act as an abstraction layer to provide a stable endpoint for accessing pods that belong to a specific application. By using labels and selectors, Kubernetes Services automatically discover and route traffic to the appropriate pods, regardless of their underlying infrastructure or location within the cluster. This enables seamless communication between microservices and facilitates the scalability and resilience of distributed applications in Kubernetes environments.
</details>

<details>
   <summary id="load-balancer">
       ⚖️ Load Balancer
   </summary>
  In Kubernetes, the built-in Load Balancer functionality is facilitated through the Kubernetes Service object. This component plays a pivotal role in distributing incoming traffic across multiple instances of an application or service deployed within a Kubernetes cluster. The Kubernetes Service abstracts away the complexities of load balancing by providing a stable endpoint, known as a ClusterIP, for accessing pods associated with a specific application or service. By leveraging labels and selectors defined in the Service configuration, Kubernetes dynamically routes incoming traffic to the appropriate pods, ensuring efficient load distribution and high availability. The routing algorithm used by the built-in Kubernetes Load Balancer is typically round-robin, which evenly distributes incoming requests among the available pods. This approach ensures that each pod receives a fair share of the incoming traffic, preventing overloading of any single pod and promoting scalability and resilience within the cluster.
</details>

<details>
   <summary id="web-server">
       🌐 Web Server
   </summary>
  The Kubernetes Ingress configuration defines the routing rules for incoming HTTP traffic to the NGINX web server within the Kubernetes cluster. Using the Ingress resource, we exposed HTTP and HTTPS routes from outside the cluster to services within the cluster, enabling external access to applications and microservices. In this configuration, the Ingress resource specifies routing rules based on the requested host and URL path. Annotations are used to configure additional behavior, such as rewriting URL paths. The Ingress resource abstracts away the complexities of managing external access to services and provides a centralized configuration for routing HTTP traffic within the Kubernetes cluster.
</details>

<details>
   <summary id="continous-integration">
       🔁 Continous Integration
   </summary>
  This project implements Continuous Integration (CI) using GitHub Actions, automating the build and deployment process for individual microservices like the Freelancer microservice. Triggered by code pushes to the "master" branch or pull requests targeting the same branch, the CI pipeline executes a tailored series of steps for each microservice. These steps include code checkout, Maven-based compilation (cleaning and packaging the app into .jar file), Docker image creation, and push to Docker Hub. By seamlessly integrating CI into the development workflow and utilizing triggers configured to activate only when modifications occur within the respective microservice directories, this approach ensures rapid feedback loops, enhanced code quality, and efficient delivery of updates to individual microservices while optimizing resource utilization and reducing unnecessary overhead.
</details>

### 📊 Logging & Monitoring

<details>
   <summary id="slf4j">
       📚 SLF4J
   </summary>
  We implemented logging functionality using the Simple Logging Facade for Java (SLF4J) framework, a widely adopted logging abstraction layer. This allows us to decouple the logging implementation from the application code, providing flexibility to switch between different logging frameworks such as Logback, Log4j, or Java Util Logging (JUL) without modifying the codebase. Our logging aspect, represented by the AppLogger class, utilizes SLF4J's logger interface to record method invocations, arguments, return values, exectution time, and exceptions. By leveraging SLF4J, we ensure consistent and standardized logging across our microservices, facilitating troubleshooting, monitoring, and performance analysis.
</details>

<details>
   <summary id="prometheus">
       🛢 Prometheus
   </summary>
  In our Kubernetes environment, we've integrated Prometheus for comprehensive monitoring and metrics collection across all services deployed within the cluster. Prometheus is configured to scrape Kubernetes metrics server data as well as Spring Boot Actuator endpoints exposed by each service. Leveraging Kubernetes service discovery, Prometheus dynamically discovers and scrapes relevant endpoints, ensuring seamless integration with new services as they are deployed. Annotations are utilized to specify scraping configurations for each service (/actuator/prometheus). This setup enables us to gather rich insights into the performance, health, and behavior of our microservices, empowering proactive monitoring, alerting, and analysis to maintain system reliability and scalability.
</details>

<details>
   <summary id="grafana">
       📶 Grafana
   </summary>
  Within our Kubernetes cluster, we've deployed Grafana as a powerful visualization and monitoring tool to complement Prometheus. Grafana is configured to connect to Prometheus as a data source using the Prometheus server's cluster IP address and the exposed container port. This integration enables Grafana to query and visualize the rich metrics collected by Prometheus, providing insights into the health, performance, and behavior of our microservices. Leveraging Grafana's intuitive dashboarding capabilities, we've created custom dashboards tailored to monitor Kubernetes-specific metrics as well as application-level metrics exposed by Prometheus. This setup empowers our team with real-time visibility into the state of our Kubernetes environment and the performance of our microservices, facilitating informed decision-making and proactive monitoring.
  
  ![Grafana](https://github.com/AWahba1/Guru.com-clone/blob/master/Screenshots/sca1.png)
  ![Grafana](https://github.com/AWahba1/Guru.com-clone/blob/master/Screenshots/sca2.png)
  ![Grafana](https://github.com/AWahba1/Guru.com-clone/blob/master/Screenshots/sca3.png)
</details>

## 📜 API documentation

### 🛡️ Authentication
Endpoints require authentication using a bearer token. Include the token in the Authorization header of your requests. 

```bash
Authorization: Bearer <your_access_token>
```

<details>
   <summary>
      Get Freelancer By Id
   </summary>
Description: Retrieves details of a specific freelancer.

- URL: http://scalabol.freelancer/freelancer/{freelancer_id}
- Method: GET

Request Parameters
- freelancer_id (path parameter): The ID of the freelancer to retrieve.
- Example Request

```bash
GET http://scalabol.freelancer/freelancer/d5a0c5f5-2911-41bc-be7b-559dd3a064b9
```

Response
- 200 OK: Returns the details of the requested freelancer.

```json
{
  "id": "d5a0c5f5-2911-41bc-be7b-559dd3a064b9",
  "name": "Freelancer1",
  "profile_views": 78,
  ...
}
```
- 404 Not Found: If the freelancer with the specified ID does not exist.

</details>

<details>
   <summary>
      Get Job By Id
   </summary>
Description: Retrieves details of a specific job.

- URL: http://scalabol.job/jobs/{job_id}
- Method: GET

Request Parameters
- job_id (path parameter): The ID of the job to retrieve.
- Example Request

```bash
GET http://scalabol.job/jobs/64d950c5-cb47-4711-b1bc-cbe88415018f
```

Response
- 200 OK: Returns the details of the requested job.

```json
{
  "id": "64d950c5-cb47-4711-b1bc-cbe88415018f",
  "title": "E-commerce Mobile App",
  "description": "Sample Description",
  ...
}
```
- 404 Not Found: If the job with the specified ID does not exist.

</details>

## 🧪 Load Testing

<details>
   <summary>
      Jmeter
   </summary>
   
![jmeter](https://i0.wp.com/cdn-images-1.medium.com/max/800/1*KeuQ7uNalz2l4rBOyPAUpg.png?w=1180&ssl=1)

We used JMeter to load test our app we configured it to simulate hundred thousands of users' requests. here are some results for different endpoints. Here are a few examples of endpoint performance.
</details>

<details>
   <summary>
      Get All Freelancers Endpoint
   </summary>
The 0.11% was due to running out of space as 5 active pods of freelancer service were logging at the same time overflowing the local host machine hard disk.

![gett all freelancers](https://github.com/AWahba1/Guru.com-clone/blob/master/Screenshots/sca4.png)
![gett all freelancers](https://github.com/AWahba1/Guru.com-clone/blob/master/Screenshots/sca5.png)

</details>

<details>
   <summary>
       Get All Jobs Filtered Endpoint
   </summary>
   
   ![get all jobs](https://github.com/AWahba1/Guru.com-clone/blob/master/Screenshots/sca6.png)
</details>
