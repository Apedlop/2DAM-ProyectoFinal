import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  Platform,
  Switch,
  LogBox,
} from "react-native";
import DateTimePicker from "@react-native-community/datetimepicker";
import UserService from "../../api/userService";
import CycleService from "../../api/cycleService";
import { colors } from "../../config/colors";
import { useUser } from "../../context/UserContext";

export default function SignUp({ onLoginSuccess }) {
  const { login } = useUser();

  const [form, setForm] = useState({
    name: "",
    surname: "",
    email: "",
    password: "",
    birthdate: "",
    isFirstCycle: true,
    lastPeriod: "",
    lastCycleLength: "",
    menstruationDuration: "",
  });

  const [step, setStep] = useState(1);
  const [errorMessage, setErrorMessage] = useState("");
  const [showDatePicker, setShowDatePicker] = useState({ field: null });
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleDateChange = (event, selectedDate) => {
    if (event.type === "set" && selectedDate) {
      const formattedDate = selectedDate.toISOString().split("T")[0];
      setForm((prev) => ({ ...prev, [showDatePicker.field]: formattedDate }));
    }
    setShowDatePicker({ field: null });
  };

  const isStepValid = () => {
    if (step === 1) {
      const { name, surname, email, password, birthdate } = form;
      if (!name.trim() || !surname.trim() || !email.trim() || !password || !birthdate) {
        setErrorMessage("Por favor completa todos los campos.");
        return false;
      }
      if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        setErrorMessage("Formato de email no válido.");
        return false;
      }
      if (password.length < 6) {
        setErrorMessage("La contraseña debe tener al menos 6 caracteres.");
        return false;
      }
    } else if (step === 2 && !form.isFirstCycle) {
      const { lastPeriod, lastCycleLength, menstruationDuration } = form;
      if (!lastPeriod || !lastCycleLength.trim() || !menstruationDuration.trim()) {
        setErrorMessage("Por favor completa todos los campos del ciclo.");
        return false;
      }
      if (
        isNaN(parseInt(lastCycleLength)) ||
        parseInt(lastCycleLength, 10) <= 0 ||
        isNaN(parseInt(menstruationDuration)) ||
        parseInt(menstruationDuration, 10) <= 0
      ) {
        setErrorMessage(
          "Duración del ciclo y duración de la menstruación deben ser números mayores que 0."
        );
        return false;
      }
    }
    setErrorMessage("");
    return true;
  };

  const handleNext = () => {
    if (isStepValid()) {
      setStep(2);
    }
  };

  const handleSignUp = async () => {
    if (!isStepValid()) return;

    try {
      const payload = { ...form };

      if (form.isFirstCycle) {
        payload.lastPeriod = new Date().toISOString().split("T")[0];
        payload.lastCycleLength = "28";
        payload.menstruationDuration = "5";
      }

      // Crear usuario
      const response = await UserService.addUser(payload);
      const createdUser = response.data;

      // Loguear usuario
      login(createdUser);

      // Crear ciclo menstrual
      const cyclePayload = {
        userId: createdUser.id,
        startDate: payload.lastPeriod,
        cycleLength: parseInt(payload.lastCycleLength, 10),
        menstruationDuration: parseInt(payload.menstruationDuration, 10),
      };

      await CycleService.addCycle(cyclePayload);

      onLoginSuccess();
    } catch (error) {
      console.error("Error en registro:", error);
      setErrorMessage("Error al registrar. Por favor, intenta nuevamente.");
    }
  };

  return (
    <ScrollView
      contentContainerStyle={styles.container}
      keyboardShouldPersistTaps="handled"
    >
      <View style={styles.card}>
        <Text style={styles.title}>Registrarse</Text>
        <Text style={styles.subtitle}>
          {step === 1 ? "Datos personales" : "Datos del ciclo"}
        </Text>

        {errorMessage ? <Text style={styles.error}>{errorMessage}</Text> : null}

        {step === 1 ? (
          <>
            <TextInput
              placeholder="Nombre"
              value={form.name}
              onChangeText={(text) => handleChange("name", text)}
              style={styles.input}
              autoCapitalize="words"
            />
            <TextInput
              placeholder="Apellido"
              value={form.surname}
              onChangeText={(text) => handleChange("surname", text)}
              style={styles.input}
              autoCapitalize="words"
            />
            <TextInput
              placeholder="Email"
              value={form.email}
              onChangeText={(text) => handleChange("email", text)}
              style={styles.input}
              keyboardType="email-address"
              autoCapitalize="none"
              autoCorrect={false}
            />
            <View style={{ position: "relative" }}>
              <TextInput
                placeholder="Contraseña"
                value={form.password}
                onChangeText={(text) => handleChange("password", text)}
                style={styles.input}
                secureTextEntry={!showPassword}
                autoCapitalize="none"
              />
              <TouchableOpacity
                onPress={() => setShowPassword((prev) => !prev)}
                style={styles.showPasswordButton}
              >
                <Text style={styles.showPasswordText}>
                  {showPassword ? "Ocultar" : "Mostrar"}
                </Text>
              </TouchableOpacity>
            </View>
            <TouchableOpacity
              style={styles.input}
              onPress={() => setShowDatePicker({ field: "birthdate" })}
            >
              <Text style={{ color: form.birthdate ? "#333" : "#aaa" }}>
                {form.birthdate || "Fecha de nacimiento"}
              </Text>
            </TouchableOpacity>
            {showDatePicker.field === "birthdate" && (
              <DateTimePicker
                value={form.birthdate ? new Date(form.birthdate) : new Date()}
                mode="date"
                display={Platform.OS === "ios" ? "spinner" : "default"}
                onChange={handleDateChange}
                maximumDate={new Date()}
              />
            )}

            <TouchableOpacity style={styles.button} onPress={handleNext}>
              <Text style={styles.buttonText}>Siguiente</Text>
            </TouchableOpacity>
          </>
        ) : (
          <>
            <View style={styles.switchRow}>
              <Text style={styles.switchLabel}>¿Es tu primer ciclo?</Text>
              <Switch
                value={form.isFirstCycle}
                onValueChange={(value) => handleChange("isFirstCycle", value)}
              />
            </View>

            {!form.isFirstCycle && (
              <>
                <TouchableOpacity
                  style={styles.input}
                  onPress={() => setShowDatePicker({ field: "lastPeriod" })}
                >
                  <Text style={{ color: form.lastPeriod ? "#333" : "#aaa" }}>
                    {form.lastPeriod || "Fecha de última menstruación"}
                  </Text>
                </TouchableOpacity>
                {showDatePicker.field === "lastPeriod" && (
                  <DateTimePicker
                    value={form.lastPeriod ? new Date(form.lastPeriod) : new Date()}
                    mode="date"
                    display={Platform.OS === "ios" ? "spinner" : "default"}
                    onChange={handleDateChange}
                    maximumDate={new Date()}
                  />
                )}
                <TextInput
                  placeholder="Duración del ciclo (días)"
                  value={form.lastCycleLength}
                  onChangeText={(text) => handleChange("lastCycleLength", text)}
                  style={styles.input}
                  keyboardType="numeric"
                />
                <TextInput
                  placeholder="Duración de la menstruación (días)"
                  value={form.menstruationDuration}
                  onChangeText={(text) =>
                    handleChange("menstruationDuration", text)
                  }
                  style={styles.input}
                  keyboardType="numeric"
                />
              </>
            )}

            <TouchableOpacity style={styles.button} onPress={handleSignUp}>
              <Text style={styles.buttonText}>Registrarse</Text>
            </TouchableOpacity>
          </>
        )}
      </View>
    </ScrollView>
  );
}
const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    justifyContent: "center",
    backgroundColor: colors.primary,
    padding: 20,
  },
  card: {
    backgroundColor: colors.goldMedium,
    borderRadius: 24,
    paddingVertical: 35,
    paddingHorizontal: 25,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.15,
    shadowRadius: 10,
    elevation: 10,
  },
  title: {
    fontSize: 36,
    fontWeight: "700",
    color: colors.primary,
    textAlign: "center",
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: colors.gray,
    textAlign: "center",
    marginBottom: 24,
  },
  input: {
    backgroundColor: colors.white,
    borderRadius: 10,
    paddingVertical: 14,
    paddingHorizontal: 18,
    fontSize: 16,
    borderWidth: 1,
    borderColor: colors.goldDark,
    marginBottom: 16,
    color: colors.black,
  },
  button: {
    backgroundColor: colors.primary,
    borderRadius: 10,
    paddingVertical: 15,
    alignItems: "center",
    marginTop: 10,
    shadowColor: colors.primary,
    shadowOpacity: 0.25,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 4,
  },
  buttonText: {
    color: colors.goldLight,
    fontSize: 17,
    fontWeight: "600",
  },
  error: {
    color: colors.white,
    backgroundColor: colors.error,
    padding: 12,
    borderRadius: 8,
    textAlign: "center",
    marginBottom: 18,
    fontSize: 14,
  },
  switchRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    marginBottom: 16,
  },
  switchLabel: {
    fontSize: 16,
    color: colors.black,
  },
  showPasswordButton: {
    position: "absolute",
    right: 10,
    top: 14,
    padding: 4,
  },
  showPasswordText: {
    color: colors.primary,
    fontSize: 14,
    fontWeight: "500",
  },
});
