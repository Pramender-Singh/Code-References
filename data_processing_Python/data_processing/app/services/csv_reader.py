import json
import logging
import pandas as pd
from typing import Dict


def log_invalid_data(record: Dict, log_file: str):
    logger = logging.getLogger(log_file)
    logger.setLevel(logging.INFO)

    handler = logging.FileHandler(log_file)
    handler.setLevel(logging.INFO)
    formatter = logging.Formatter("%(asctime)s - %(levelname)s - %(message)s")
    handler.setFormatter(formatter)
    if not logger.handlers:
        logger.addHandler(handler)
    logger.info(f"Invalid Record: {record}")
    handler.flush()


def read_data(file_path: str, output_file: str, country_to_region: dict):

    try:

        data = pd.read_csv(file_path)
        required_columns = ["name", "email", "age", "country"]
        if not all(column in data.columns for column in required_columns):
            return {
                "status": "error",
                "message": f"Missing required columns. Required: {required_columns}",
            }

        data.dropna(subset=required_columns, inplace=True)

        invalid_records = []

        df_cleaned = []
        for _, row in data.iterrows():
            record = row.to_dict()

            age = str(record["age"]).strip()
            if age.isdigit():
                age = int(age)
            else:

                log_invalid_data(record, "app/output/invalid_data.log")
                invalid_records.append(record)
                continue

            if age < 18:
                log_invalid_data(record, "app/output/under_age.log")
                invalid_records.append(record)
                continue

            record["name"] = str(record["name"]).title()  # Title case for names
            record["email"] = str(record["email"]).strip().lower()  # Normalize email
            record["country"] = str(record["country"]).title()

            country = record.get("country")
            record["region"] = get_region(country, country_to_region)

            df_cleaned.append(record)

        df_cleaned_df = pd.DataFrame(df_cleaned)
        df_cleaned_df.to_csv(output_file, index=False)

        logging.info(f"Processed {len(df_cleaned)} valid records and saved to {output_file}.")
        logging.info(f"Logged {len(invalid_records)} invalid records.")

        return {
            "status": "success",
            "message": f"Successfully processed {len(df_cleaned)} valid records.",
            "output_file": output_file,
            "invalid_records": invalid_records,  # Include invalid records for review
        }

    except Exception as e:

        return {"status": "error", "message": str(e)}


def get_region(country, country_to_region):

    country = str(country).strip().lower()
    for region, countries in country_to_region.items():
        if country in [c.lower() for c in countries]:
            return region
    return "Other"
