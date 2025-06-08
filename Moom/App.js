import * as Notifications from "expo-notifications";
import React, { useEffect, useState } from "react";
import { NavigationContainer } from "@react-navigation/native";
import TabNavigation from "./src/navigation/TabNavigation";
import AuthStack from "./src/navigation/AuthStack";
import Toast from "react-native-toast-message";
import { UserProvider } from "./src/context/UserContext";
import { LogBox, ActivityIndicator, View } from "react-native";
import { useUser } from "./src/context/UserContext";
import { colors } from "./src/config/colors";

LogBox.ignoreLogs([
  "expo-notifications: Android Push notifications (remote notifications) functionality provided by expo-notifications was removed from Expo Go",
  "`expo-notifications` functionality is not fully supported in Expo Go",
  "The action 'RESET' with payload",
  "onLoginSuccess is not a function",
]);

export default function App() {
  useEffect(() => {
    const requestPermissions = async () => {
      const { status } = await Notifications.requestPermissionsAsync();
      if (status !== "granted") {
        console.warn("Permisos de notificación no concedidos");
      }
      console.log("Permisos de notificación concedidos:", status);
    };
    requestPermissions();
  }, []);

  const RootNavigation = () => {
    const { isAuthenticated, isLoading } = useUser();

    if (isLoading) {
      return (
        <View
          style={{ flex: 1, justifyContent: "center", alignItems: "center" }}
        >
          <ActivityIndicator size="large" color={colors.primary} />
        </View>
      );
    }

    return (
      <NavigationContainer>
        {isAuthenticated ? <TabNavigation /> : <AuthStack />}
      </NavigationContainer>
    );
  };

  return (
    <UserProvider>
      <RootNavigation />
      <Toast />
    </UserProvider>
  );
}
