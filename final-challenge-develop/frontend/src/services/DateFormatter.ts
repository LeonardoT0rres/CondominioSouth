export function dateFormat(date: string) {
  const fullDate = new Date(date).toLocaleString('pt-BR', {
    timeZone: 'UTC',
    year: '2-digit',
    hour: '2-digit',
    day: '2-digit',
    month: '2-digit',
    minute: '2-digit',
  });

  const dateDDMMYY = fullDate.slice(0, 8);
  const dateHour = (parseInt(fullDate.slice(9, 12)) - 6);
  const dateMinute = fullDate.slice(12);

  const dateFormatted = `Dia ${dateDDMMYY}, ${dateHour}h${dateMinute}min`;
  
  return dateFormatted;
}

// Esta função corrige um problema padrão dos navegadores para digitação em Inputs com tipos: date, datetime e datetime-local. Que é basicamente aceitar até 5 ou 6 dígitos no ano. A função corta esse 5º ou 6º dígito para ficar só os 4 primeiros dígitos para o ano:
export function dateYearDigitCorrector(correctDate: string) {
  const year = correctDate.substring(
    0,
    correctDate.indexOf('-')
  );

  const restDate = correctDate.substring(
    correctDate.indexOf('-')
  );

  if (year.length > 4) {
    const newYear = year.substring(0, 4);
    correctDate = newYear + restDate;
  }

  return correctDate;
}
