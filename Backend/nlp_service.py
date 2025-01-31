from flask import Flask, request, jsonify
from transformers import pipeline

app = Flask(__name__)

print("Initializing the NLP model...")
# Load the Hugging Face model
classifier = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")
print("Model loaded successfully.")

@app.route("/analyze", methods=["POST"])
def analyze_comment():
    try:
        print("Received request at /analyze")

        # Get the comment from the POST request
        data = request.json
        print(f"Request data: {data}")

        comment = data.get("comment")
        if not comment:
            print("Error: Comment is missing in the request.")
            return jsonify({"error": "Comment is required"}), 400
        
        # Define labels
        labels = ["properly utilized", "not properly utilized"]
        print(f"Analyzing comment: {comment}")
        
        # Analyze the comment
        result = classifier(comment, candidate_labels=labels)
        print(f"Analysis result: {result}")

        response = {
            "comment": comment,
            "labels": result["labels"],
            "scores": result["scores"]
        }
        print(f"Response to be sent: {response}")
        return jsonify(response), 200
        
    except Exception as e:
        print(f"Error occurred: {e}")
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
