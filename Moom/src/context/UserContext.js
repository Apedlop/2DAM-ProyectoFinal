import React, { createContext, useContext, useState, useEffect } from "react";
import UserService from "../api/userService";
import AsyncStorage from "@react-native-async-storage/async-storage";

const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  const logout = async () => {
    try {
      await AsyncStorage.removeItem("@user");
      setUser(null);
      setIsAuthenticated(false);
      return true;
    } catch (error) {
      console.error("Logout error:", error);
      return false;
    }
  };

  useEffect(() => {
    const loadUser = async () => {
      try {
        const storedUserStr = await AsyncStorage.getItem("@user");
        
        // Si no hay usuario almacenado, hacer logout
        if (!storedUserStr) {
          await logout();
          return;
        }

        const storedUser = JSON.parse(storedUserStr);

        // Verificar con la API si el usuario existe
        try {
          const response = await UserService.getUser(storedUser.id);
          if (response?.data) {
            setUser(response.data);
            setIsAuthenticated(true);
          } else {
            await logout();
          }
        } catch (error) {
          await logout();
        }
      } catch (error) {
        console.error("Error loading user:", error);
        await logout();
      } finally {
        setIsLoading(false);
      }
    };

    loadUser();
  }, []);

  const login = async (userData) => {
    try {
      await AsyncStorage.setItem("@user", JSON.stringify(userData));
      setUser(userData);
      setIsAuthenticated(true);
      return true;
    } catch (error) {
      console.error("Login error:", error);
      return false;
    }
  };

  return (
    <UserContext.Provider
      value={{
        user,
        isAuthenticated,
        isLoading,
        login,
        logout,
        setUser,
      }}
    >
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => useContext(UserContext);