from flask import Flask, send_file, request
import torch 
import soundfile as sf
from kokoro import KPipeline

device = "cuda" if torch.cuda.is_available() else "cpu"

english_pipeline = KPipeline(lang_code='a', device=device)
french_pipeline = KPipeline(lang_code='f', device=device)

app = Flask(__name__)

@app.route("/tts-service", methods=['POST'])
def tts_service(): 
    # Notification comes: app name, title, text, language, voice. 
    data = request.get_json()
    language = "english"
    voice = "af_heart"

    # Preprocess if necessary 
    text = f"{data['app']}. {data['title']}. {data['text']}"
    print(text)

    match language: 
        case "english": 
            generator = english_pipeline(text, voice=voice)
        case "french": 
            generator = french_pipeline(text, voice=voice)
        case _: 
            generator = english_pipeline(text, voice=voice)

    # Generate wav file
    for i, (_, _, audio) in enumerate(generator):
        sf.write(f'output.wav', audio*2, 24000)

    return send_file("output.wav", mimetype="audio/wav")

if __name__ == "__main__":
    app.run(host="0.0.0.0", port = 5000)
