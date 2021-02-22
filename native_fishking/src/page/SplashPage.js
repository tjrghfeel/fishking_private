import React from 'react';
import {View, Image} from 'react-native';
import {inject, observer} from 'mobx-react';

export default inject('WebViewStore')(
  observer(
    class extends React.Component {
      componentDidMount() {
        const {navigation} = this.props;
        setTimeout(() => {
          navigation.navigate('webview');
        }, 1500);
      }
      render() {
        return (
          <View
            style={{
              backgroundColor: '#FFF',
              flex: 1,
              justifyContent: 'center',
              alignItems: 'center',
              paddingLeft: 22,
              paddingRight: 22,
            }}>
            <Image source={require('../images/jenkins.png')} />
          </View>
        );
      }
    },
  ),
);
