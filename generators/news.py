import requests
import time
from faker import Faker
from datetime import datetime

API_BASE = "http://localhost:8080/re-center/api"
ADMIN_EMAIL = "guest@example.com"
ADMIN_PASSWORD = "123456"

NEWS_COUNT = 10
REQUEST_DELAY = 1

fake = Faker("ru_RU")

def login():
    """Авторизоваться и получить JWT токен."""
    url = f"{API_BASE}/auth/login"
    payload = {"email": ADMIN_EMAIL, "password": ADMIN_PASSWORD}
    resp = requests.post(url, json=payload)
    resp.raise_for_status()
    data = resp.json()
    token = data.get("token")
    if not token:
        raise Exception("Токен не найден в ответе")
    return token

def create_news(token, title, content, image_url=None, status="PUBLISHED"):
    """Создать новость через API."""
    url = f"{API_BASE}/news"
    headers = {"Authorization": f"Bearer {token}", "Content-Type": "application/json"}
    payload = {
        "title": title,
        "content": content,
        "imageUrl": image_url,
        "status": status
    }
    resp = requests.post(url, json=payload, headers=headers)
    resp.raise_for_status()
    return resp.json()

def generate_news_item():
    """Генерирует один заголовок и текст новости."""
    title = fake.sentence(nb_words=5, variable_nb_words=True)[:-1]
    paragraphs = [fake.paragraph(nb_sentences=4) for _ in range(fake.random_int(3, 5))]
    content = "\n\n".join(paragraphs)
    image_url = None
    return title, content, image_url

def main():
    print("Авторизация...")
    token = login()
    print("Успешно. Начинаем генерацию новостей...")

    for i in range(1, NEWS_COUNT + 1):
        title, content, img = generate_news_item()
        print(f"Создание #{i}: {title}")
        try:
            result = create_news(token, title, content, img, status="PUBLISHED")
            print(f"  -> ID {result.get('id')}")
        except Exception as e:
            print(f"  Ошибка: {e}")
        time.sleep(REQUEST_DELAY)

    print("Готово.")

if __name__ == "__main__":
    main()