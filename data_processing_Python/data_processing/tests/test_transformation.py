import os
import sys
import pytest
import json
from ..app.services.csv_reader import read_data

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '../app')))

from main import load_country_to_region

@pytest.fixture
def config():
    with open("config.json", 'r') as file:
        return json.load(file)

@pytest.fixture
def country_to_region():
    return load_country_to_region("region.json")

@pytest.fixture(autouse=True)
def cleanup(config):

    if os.path.exists(config["output_file"]):
        os.remove(config["output_file"])
    if os.path.exists("invalid_data.log"):
        os.remove("invalid_data.log")
    if os.path.exists("under_age.log"):
        os.remove("under_age.log")
    yield

    if os.path.exists(config["output_file"]):
        os.remove(config["output_file"])
    if os.path.exists("../app/output/invalid_data.log"):
        os.remove("invalid_data.log")
    if os.path.exists("under_age.log"):
        os.remove("under_age.log")


def test_read_data(config, country_to_region):
    result = read_data(config["input_file"], config["output_file"], country_to_region)

    assert result["status"] == "success"
    assert "Successfully processed" in result["message"]
    assert os.path.exists(config["output_file"])

    with open(config["invalid_log"], 'r') as log_file:
        invalid_data = log_file.readlines()
        assert len(invalid_data) > 0, "Expected some invalid records to be logged."

    with open(config["underage_log"], 'r') as under_age_file:
        under_age_data = under_age_file.readlines()
        assert len(under_age_data) > 0, "Expected some underage records to be logged."
