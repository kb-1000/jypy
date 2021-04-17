# invoke as PYTHONPATH=../../.. python -m tokenize_tokenize from src/main/resources
import json
import tokenize

for file in ("tokenize.py", "token.py"):
    with open(file, "rb") as fp:
        tokens = list(tokenize.tokenize(fp.__next__))

    with open(f"{file}.json", "w", encoding="utf-8", newline="\n") as fp:
        json.dump(tokens, fp)
