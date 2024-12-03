import json
from app.services.csv_reader import read_data


def load_country_to_region(file_path: str) -> dict:
    try:
        with open(file_path, 'r') as file:
            country_to_region = json.load(file)
        return country_to_region
    except Exception as e:
        print(f"Error loading country-to-region mapping: {e}")
        return {}


if __name__ == '__main__':

    try:
        with open('config.json', 'r') as config_file:
            config = json.load(config_file)

        input_file = config.get('input_file')
        output_file = config.get('output_file')
        country_to_region_file = config.get('country_to_region')
        country_to_region = load_country_to_region(country_to_region_file)

        if not input_file or not output_file:
            raise ValueError("Missing input_file or output_file in the config.")

        result = read_data(input_file, output_file, country_to_region)

        print(result)

    except FileNotFoundError:
        print("Configuration file 'config.json' not found.")
    except json.JSONDecodeError:
        print("Error reading the configuration file.")
    except ValueError as e:
        print(f"Error: {e}")




