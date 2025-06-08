import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ActivityIndicator,
  ScrollView,
} from "react-native";
import { useNavigation } from "@react-navigation/native";
import { colors } from "../../config/colors";
import Toast from "react-native-toast-message";
import { useUser } from "../../context/UserContext";
import UserService from "../../api/userService";
import Icon from "react-native-vector-icons/MaterialIcons";

const ForgotPassword = () => {
  const [step, setStep] = useState(1);
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [passwordVisible, setPasswordVisible] = useState(false);
  const { user } = useUser();
  const navigation = useNavigation();
  const [emailValid, setEmailValid] = useState(false);
  const [userId, setUserId] = useState(null);

  // Validar email mientras se escribe
  useEffect(() => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    setEmailValid(emailRegex.test(email));
  }, [email]);

  const handleEmailSubmit = async () => {
    if (!emailValid) {
      setError("Por favor ingresa un correo electrónico válido");
      return;
    }

    setIsLoading(true);
    setError("");

    try {
      const response = await UserService.getAll(); // obtener todos los usuarios
      const users = response.data;
      const foundUser = users.find((user) => user.email === email);

      if (foundUser) {
        setUserId(foundUser.id);
        setStep(2);
      } else {
        setError("Correo no encontrado. Por favor verifica tu dirección.");
      }
    } catch (err) {
      console.error("Error al obtener usuarios:", err);
      setError("Error al verificar el correo. Por favor intenta más tarde.");
    } finally {
      setIsLoading(false);
    }
  };

  const handlePasswordSubmit = async () => {
    if (newPassword.length < 6) {
      setError("La contraseña debe tener al menos 6 caracteres");
      return;
    }

    if (newPassword !== confirmPassword) {
      setError("Las contraseñas no coinciden");
      return;
    }

    setIsLoading(true);
    setError("");

    try {
      // Obtener el usuario completo
      const response = await UserService.getUser(userId);
      const existingUser = response.data;

      // Actualizar solo el campo password
      const updatedUser = {
        ...existingUser,
        password: newPassword,
      };

      await UserService.updateUser(userId, updatedUser);

      Toast.show({
        type: "success",
        text1: "Contraseña actualizada",
        text2: "Tu contraseña ha sido cambiada exitosamente",
        visibilityTime: 3000,
      });
      navigation.navigate("LogIn");
    } catch (err) {
      console.error("Error al cambiar contraseña:", err);
      setError("Error al actualizar la contraseña. Intenta más tarde.");
    } finally {
      setIsLoading(false);
    }
  };

  const goBack = () => {
    if (step === 1) {
      navigation.goBack();
    } else {
      setStep(1);
      setNewPassword("");
      setConfirmPassword("");
    }
  };

  return (
    <View style={styles.container}>
      <ScrollView
        contentContainerStyle={styles.scrollContainer}
        keyboardShouldPersistTaps="handled"
      >
        <View style={styles.card}>
          <TouchableOpacity onPress={goBack} style={styles.backButton}>
            <Icon name="arrow-back" size={24} color={colors.primary} />
          </TouchableOpacity>

          <Text style={styles.title}>
            {step === 1 ? "Recuperar contraseña" : "Crear nueva contraseña"}
          </Text>

          {step === 1 ? (
            <View>
              <Text style={styles.subtitle}>
                Ingresa tu correo electrónico para recuperar tu cuenta
              </Text>

              <Text style={styles.label}>Correo electrónico</Text>
              <View style={styles.inputContainer}>
                <TextInput
                  style={styles.input}
                  value={email}
                  onChangeText={setEmail}
                  keyboardType="email-address"
                  autoCapitalize="none"
                  autoCorrect={false}
                  placeholder="ejemplo@correo.com"
                  placeholderTextColor={colors.gray}
                />
                {email.length > 0 && (
                  <TouchableOpacity
                    onPress={() => setEmail("")}
                    style={styles.clearIcon}
                  >
                    <Icon name="close" size={20} color={colors.gray} />
                  </TouchableOpacity>
                )}
              </View>

              <TouchableOpacity
                style={[styles.button, !emailValid && styles.buttonDisabled]}
                onPress={handleEmailSubmit}
                disabled={!emailValid || isLoading}
              >
                {isLoading ? (
                  <ActivityIndicator color={colors.white} />
                ) : (
                  <Text style={styles.buttonText}>Continuar</Text>
                )}
              </TouchableOpacity>
            </View>
          ) : (
            <View>
              <Text style={styles.subtitle}>
                Crea una nueva contraseña segura para tu cuenta
              </Text>

              <Text style={styles.label}>Nueva contraseña</Text>
              <View style={styles.inputContainer}>
                <TextInput
                  style={styles.input}
                  value={newPassword}
                  onChangeText={setNewPassword}
                  secureTextEntry={!passwordVisible}
                  placeholder="Mínimo 6 caracteres"
                  placeholderTextColor={colors.gray}
                />
                <TouchableOpacity
                  onPress={() => setPasswordVisible(!passwordVisible)}
                  style={styles.visibilityIcon}
                >
                  <Icon
                    name={passwordVisible ? "visibility-off" : "visibility"}
                    size={20}
                    color={colors.gray}
                  />
                </TouchableOpacity>
              </View>

              <Text style={styles.label}>Confirmar contraseña</Text>
              <View style={styles.inputContainer}>
                <TextInput
                  style={styles.input}
                  value={confirmPassword}
                  onChangeText={setConfirmPassword}
                  secureTextEntry={!passwordVisible}
                  placeholder="Repite tu contraseña"
                  placeholderTextColor={colors.gray}
                />
              </View>

              <TouchableOpacity
                style={[
                  styles.button,
                  (newPassword.length < 6 || newPassword !== confirmPassword) &&
                    styles.buttonDisabled,
                ]}
                onPress={handlePasswordSubmit}
                disabled={
                  newPassword.length < 6 ||
                  newPassword !== confirmPassword ||
                  isLoading
                }
              >
                {isLoading ? (
                  <ActivityIndicator color={colors.white} />
                ) : (
                  <Text style={styles.buttonText}>Cambiar contraseña</Text>
                )}
              </TouchableOpacity>
            </View>
          )}

          {error ? (
            <View style={styles.errorContainer}>
              <Icon
                name="error-outline"
                size={20}
                color={colors.error}
                style={styles.errorIcon}
              />
              <Text style={styles.errorText}>{error}</Text>
            </View>
          ) : null}
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.primary,
  },
  scrollContainer: {
    flexGrow: 1,
    justifyContent: "center",
    paddingHorizontal: 20,
  },
  card: {
    backgroundColor: colors.goldMedium,
    borderRadius: 24,
    paddingVertical: 30,
    paddingHorizontal: 25,
    marginHorizontal: 10,
    shadowColor: colors.black,
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.15,
    shadowRadius: 10,
    elevation: 10,
  },
  backButton: {
    position: "absolute",
    top: 16,
    left: 16,
    zIndex: 1,
  },
  title: {
    fontSize: 30,
    marginTop: 20,
    fontWeight: "700",
    color: colors.primary,
    textAlign: "center",
    marginBottom: 6,
  },
  subtitle: {
    fontSize: 14,
    color: "#7F8C8D",
    textAlign: "center",
    marginBottom: 24,
  },
  label: {
    marginBottom: 8,
    color: "#600000",
    fontWeight: "500",
    fontSize: 14,
  },
  inputContainer: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderRadius: 8,
    marginBottom: 16,
    backgroundColor: colors.white,
    fontSize: 16,
    borderColor: colors.goldDark,
    color: colors.black,
  },
  input: {
    flex: 1,
    color: "#000000",
    padding: 12,
    fontSize: 16,
  },
  clearIcon: {
    padding: 10,
  },
  visibilityIcon: {
    padding: 10,
  },
  button: {
    backgroundColor: colors.primary,
    borderRadius: 10,
    paddingVertical: 15,
    alignItems: "center",
    marginTop: 5,
    shadowColor: colors.primary,
    shadowOpacity: 0.25,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 4,
  },
  buttonDisabled: {
    backgroundColor: "#BDC3C7",
    opacity: 0.7,
  },
  buttonText: {
    color: "#FFFFFF",
    fontWeight: "bold",
    fontSize: 16,
  },
  errorContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginTop: 16,
    padding: 12,
    backgroundColor: "#FDEDEC",
    borderRadius: 8,
  },
  errorIcon: {
    marginRight: 8,
  },
  errorText: {
    color: "#E74C3C",
    flex: 1,
    fontSize: 14,
  },
});

export default ForgotPassword;
