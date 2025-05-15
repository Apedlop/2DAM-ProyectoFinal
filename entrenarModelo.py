import pandas as pd

# ========================================
# UNIFICACIÓN DE LOS DATASETS DE ENTRADA
# ========================================

# Cargar los datasets de entrada
df1 = pd.read_csv('datasetCicloMenstreual/FedCycleData071012 (2).csv')
df2 = pd.read_csv('datasetCicloMenstreual/menstrual_cycle_dataset_with_factors.csv')
df3 = pd.read_csv('datasetCicloMenstreual/Menstural_cyclelength.csv')

# Estandarizar las columnas claves de los datasets
