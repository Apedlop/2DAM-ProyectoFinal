import React from 'react';
import { View, StyleSheet } from 'react-native';
import Svg, { G, Circle, Text as SvgText } from 'react-native-svg';

const CYCLE_DAYS = 28;
const currentDayInCycle = 25;

const PHASES = [
  { label: 'Menstrual', days: 5, color: '#f28b82' },
  { label: 'Folicular', days: 9, color: '#aecbfa' },
  { label: 'Ovulación', days: 1, color: '#fff475' },
  { label: 'Lútea', days: 13, color: '#d7aefb' },
];

const darkenColor = (hex: string) => {
  const factor = 0.7;
  const num = parseInt(hex.slice(1), 16);
  const r = Math.floor(((num >> 16) & 255) * factor);
  const g = Math.floor(((num >> 8) & 255) * factor);
  const b = Math.floor((num & 255) * factor);
  return `rgb(${r},${g},${b})`;
};

export default function CycleProgressCircle() {
  const radius = 100;
  const strokeWidth = 22;
  const totalAngle = 300;
  const gap = 60;
  const startAngle = gap / 2;
  const circumference = 2 * Math.PI * radius;
  const arcLength = (angle: number) => (angle / 360) * circumference;

  let rotation = startAngle;
  let dayCount = 0;

  // 🔵 Cálculo de ángulo y coordenadas
  const progressAngle = (currentDayInCycle / CYCLE_DAYS) * totalAngle + startAngle;
  const angleRad = (progressAngle - 90) * (Math.PI / 180);
  const markerX = 125 + radius * Math.cos(angleRad);
  const markerY = 125 + radius * Math.sin(angleRad);

  return (
    <View style={styles.container}>
      <Svg width={250} height={250}>
        <G origin="125, 125" rotation={-90}>
          {PHASES.map((phase, index) => {
            const phaseAngle = (phase.days / CYCLE_DAYS) * totalAngle;
            const isFilled = dayCount + phase.days <= currentDayInCycle;
            const partiallyFilled = dayCount < currentDayInCycle && currentDayInCycle < dayCount + phase.days;

            const filledDays = Math.min(currentDayInCycle - dayCount, phase.days);
            const filledAngle = (filledDays / CYCLE_DAYS) * totalAngle;

            const baseArc = (
              <Circle
                key={`base-${index}`}
                cx="125"
                cy="125"
                r={radius}
                stroke={phase.color}
                strokeWidth={strokeWidth}
                strokeDasharray={`${arcLength(phaseAngle)}, ${circumference}`}
                rotation={rotation}
                origin="125, 125"
                strokeLinecap="round"
                fill="none"
              />
            );

            const fillArc = isFilled || partiallyFilled ? (
              <Circle
                key={`fill-${index}`}
                cx="125"
                cy="125"
                r={radius}
                stroke={darkenColor(phase.color)}
                strokeWidth={strokeWidth}
                strokeDasharray={`${arcLength(Math.min(phaseAngle, filledAngle))}, ${circumference}`}
                rotation={rotation}
                origin="125, 125"
                strokeLinecap="round"
                fill="none"
              />
            ) : null;

            rotation += phaseAngle;
            dayCount += phase.days;

            return [baseArc, fillArc];
          })}
        </G>

        {/* 🔵 Bolita de día actual */}
        <Circle
          cx={markerX}
          cy={markerY}
          r={14}
          fill="#e91e63"
          stroke="#fff"
          strokeWidth={2}
        />

        {/* 🧾 Número del día dentro de la bolita */}
        <SvgText
          x={markerX}
          y={markerY + 4} // ajuste para centrar verticalmente
          fill="#fff"
          fontSize="10"
          fontWeight="bold"
          textAnchor="middle"
        >
          {currentDayInCycle}
        </SvgText>
      </Svg>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 40,
  },
});
