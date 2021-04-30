import messaging from '@react-native-firebase/messaging';

messaging()
  .getInitialNotification()
  .then(result => {
    console.log(
      '[Messaging] When the application is opened from a quit state.',
    );
  });
messaging().onNotificationOpenedApp(message => {
  console.log(
    '[Messaging] When the application is running, but in the background.',
  );
});
messaging().setBackgroundMessageHandler(async message => {
  console.log('[Messaging] Background Mode');
  console.log(JSON.stringify(message));
  // PushNotification.localNotification({
  //   title: 'Notification',
  //   message: '로컬에서 호출된 알림 메세 지 입니다.',
  //   channelId: 'notification.native_fishking',
  // });
});
messaging().onMessage(message => {
  console.log('[Messaging] Foreground Mode');
  console.log(JSON.stringify(message));
});
export let token = null;
export default (async () => {
  const hasPermission = await messaging().hasPermission();
  if (
    hasPermission !== messaging.AuthorizationStatus.AUTHORIZED &&
    hasPermission !== messaging.AuthorizationStatus.PROVISIONAL
  ) {
    await messaging().requestPermission();
  }
  token = await messaging().getToken();
  console.log('[Messaging] token -> ' + token);
})();
