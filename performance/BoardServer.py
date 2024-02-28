from locust import HttpUser, task, between
import random

class BoardServer(HttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        self.client.post("/users/sign-in", json={"userId": "test", "password": "123"})


    @task
    def view_search(self):
        sortStatus = random.choice(["CATEGORIES", "NEWEST", "OLDEST", "HIGHPRICE", "LOWPRICE", "GRADE"])
        categoryId = random.randint(1, 10)
        name = "게시글 " + str(random.randint(1, 100000))
        headers = {"Content-Type": "application/json"}
        data = {
            "sortStatus": sortStatus,
            "categoryId": categoryId,
            "name": name
        }

        self.client.post("/search", json=data, headers=headers)



