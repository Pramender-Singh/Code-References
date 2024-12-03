# Assessment
```
Python 3.12
pandas library
pytest for testing
```
# Install Requirements using below command
```
create virtual environment using below command, environment_name can be any virtual name
python3 -m venv {environment_name}

activate environment by using below command
source {environment_name}/bin/activate works for linux only.

pip install -r requirements.txt
```
# Below file specify the path for input and output file we have to change the input path on different machine
```config.json``` 
# contains the country-to-region mapping
```region.json``` 

# Run the Application
```
Place your input file (e.g., customers.csv) in the input/ folder.
python app/main.py
```
# Run Tests
```
Run unit tests using pytest:
pytest

or

pytest tests/

or

pytest tests/test_transformation.py
```

# Output file stored in app/output
```customers_transformed.csv```

# Invalid data stored in app/output 
```invalid_data.log```

# Under age stored in app/output
```under_age.log file```
