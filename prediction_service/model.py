import torch
import torch.nn as nn
import torch.optim as optim
from torch.utils.data import DataLoader, TensorDataset
from sklearn.metrics import classification_report
from data_prep import prepare_data


print(f"PyTorch Version: {torch.__version__}")
print(f"CUDA Availability: {torch.cuda.is_available()}")

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

if device.type == 'cuda':
    print(f"GPU: {torch.cuda.get_device_name(0)}")


class TradingModel(nn.Module):
    def __init__(self):
        super(TradingModel, self).__init__()

        self.conv = nn.Conv1d(in_channels=8, out_channels=32, kernel_size=3, padding=1)
        self.pool = nn.MaxPool1d(kernel_size=2)
        self.relu = nn.ReLU()
        self.dropout_cnn = nn.Dropout(0.2)

        self.lstm = nn.LSTM(input_size=32, hidden_size=128, num_layers=2, batch_first=True, dropout=0.2)
        self.fc1 = nn.Linear(128, 64)
        self.fc2 = nn.Linear(64, 3)
        self.sigmoid = nn.Sigmoid()

    def forward(self, x):
        # CNN
        x = x.permute(0, 2, 1)
        x = self.conv(x)
        x = self.pool(x)
        x = self.relu(x)
        x = self.dropout_cnn(x)

        # LSTM
        x = x.permute(0, 2, 1)
        out, _ = self.lstm(x)
        last_step_out = out[:, -1, :]

        # Classify
        x = self.fc1(last_step_out)
        x = self.sigmoid(x)
        x = self.fc2(x)

        return x


def load_model(path):
    model = TradingModel()
    model.load_state_dict(torch.load(path, map_location=device))
    model = model.to(device)

    print("Model loaded from: " + path)

    return model


def save_model(model, path):
    torch.save(model.state_dict(), path)

    print("Model saved to: " + path)


def train_model(X_train, Y_train):
    print("Training model...")

    # Prepare data
    train_dataset = TensorDataset(X_train, Y_train)
    train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)

    # Run training
    model = TradingModel()
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(model.parameters(), lr=0.001)
    epochs = 50

    model = model.to(device)

    for epoch in range(epochs):
        model.train()
        running_loss = 0.0

        for inputs, labels in train_loader:
            inputs, labels = inputs.to(device), labels.to(device)

            optimizer.zero_grad()

            outputs = model(inputs)

            loss = criterion(outputs, labels)
            loss.backward()

            optimizer.step()

            running_loss += loss.item()

        print(f"Epoch {epoch+1}/{epochs}, Loss: {running_loss/len(train_loader):.4f}")

    print("Finished training model!")

    return model


def test_model(model, X_test, Y_test):
    print("Begining of test...")

    # Prepare data
    test_dataset = TensorDataset(X_test, Y_test)
    test_loader = DataLoader(test_dataset, batch_size=32, shuffle=False)

    all_preds = []
    all_labels = []

    # Run test
    model.eval()

    with torch.no_grad():
        for inputs, labels in test_loader:
            inputs, labels = inputs.to(device), labels.to(device)

            outputs = model(inputs)
            _, predicted = torch.max(outputs, 1)
            _, lab = torch.max(labels, 1)

            all_preds.extend(predicted.cpu().numpy())
            all_labels.extend(lab.cpu().numpy())

    # Report
    print("\t\t\t> TEST REPORT <")
    print(classification_report(all_labels, all_preds, target_names=['UP', 'DOWN', 'STAY']))


def run_model(model, inputs):
    print("Running model...")

    model.eval()

    with torch.no_grad():
        inputs = inputs.to(device)

        outputs = model(inputs)
        probabilities = torch.nn.functional.softmax(outputs, dim=1).cpu()

    print("Model evaluated successfully!")

    return probabilities


if __name__ == "__main__":
    X_train, X_test, Y_train, Y_test = prepare_data()
    # model = train_model(X_train, Y_train)
    # save_model(model, "./model/model.pth")
    model = load_model("./model/model.pth")
    test_model(model, X_test, Y_test)
