# invoke as PYTHONPATH=../../.. python -m tokenize_tokenize from src/main/resources
import json
import token
import tokenize

for file in ("tokenize.py", "token.py"):
    with open(file, "rb") as fp:
        tokens = list(map(lambda token_info: (token_info if token_info.type != token.OP else tokenize.TokenInfo(
            tokenize.EXACT_TOKEN_TYPES[token_info.string], token_info.string, token_info.start, token_info.end,
            token_info.line)) if token_info.type != token.ENDMARKER else tokenize.TokenInfo(-1, token_info.string,
                                                                                            token_info.start,
                                                                                            token_info.end,
                                                                                            token_info.line), filter(
            lambda
                token_info: token_info.type != token.ENCODING and token_info.type != token.NL and token_info.type != token.COMMENT,
            tokenize.tokenize(fp.__next__))))

    with open(f"{file}.json", "w", encoding="utf-8", newline="\n") as fp:
        json.dump(tokens, fp)

with open("tokens.json", "w", encoding="utf-8", newline="\n") as fp:
    json.dump(token.tok_name, fp)

keywords = ["def", "del", "pass", "break", "continue", "return", "raise", "from", "import", "as", "global", "nonlocal",
            "assert", "if", "elif", "else", "while", "for", "in", "try", "finally", "with", "except", "lambda", "or",
            "and", "not", "is", "None", "True", "False", "class", "yield"]

with open("../antlr/PythonPython3.tokens", "w", encoding="utf-8", newline="\n") as fp:
    for number, token_name in token.tok_name.items():
        if token_name not in {"NT_OFFSET", "N_TOKENS", "ENDMARKER", "NL", "ENCODING", "COMMENT"}:
            fp.write(f"{token_name}={number}\n")

    n_tokens = token.N_TOKENS

    for keyword in keywords:
        fp.write(f"{keyword.upper()}={n_tokens}\n")
        n_tokens += 1

    for content, number in token.EXACT_TOKEN_TYPES.items():
        fp.write(f"{content!r}={number}\n")

    n_tokens = token.N_TOKENS

    for keyword in keywords:
        fp.write(f"{keyword!r}={n_tokens}\n")
        n_tokens += 1

with open("../java/com/github/kb1000/jypy/parser/tokenizer/IdentifierConverter.java", "w", encoding="utf-8",
          newline="\n") as fp:
    fp.write(f"""\
package com.github.kb1000.jypy.parser.tokenizer;

import static com.github.kb1000.jypy.parser.antlr.Python3.*;

public class IdentifierConverter {{
    public static int convertKeyword(String text) {{
        switch (text) {{
""")

    n_tokens = token.N_TOKENS

    for keyword in keywords:
        fp.write(f"""\
        case "{keyword}":
            return {keyword.upper()};
""")

    fp.write("""\
        default:
            return NAME;
        }
    }
}""")
