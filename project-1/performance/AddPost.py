from locust import HttpUser, task, between
import random

class AddPosts(HttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        self.client.post("/users/sign-in", json={"userId": "test", "password": "123"})


    @task
    def add_post(self):
        self.client.post("/posts", json={
            "name": "게시글 " + str(random.randint(1, 100000)),
            "contents": "게시글 내용 " + str(random.randint(1, 100000)),
            "categoryId": random.randint(1, 10),
            "fileId": random.randint(1, 10),
        })

