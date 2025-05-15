import CycleCircle from '../../components/TodoList/index';
import { View, Text } from 'react-native';
import React from 'react';

export default function HomeScreen() {
  return (
    <View style={{ flex: 1 }}>
      <Text style={{ textAlign: 'center', fontSize: 22, marginTop: 20 }}>
        Tu ciclo menstrual
      </Text>
      <CycleCircle />
    </View>
  );
}
