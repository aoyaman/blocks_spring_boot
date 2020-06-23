import React, { useEffect } from 'react';
import request from 'superagent';

import logo from './logo.svg';
import './App.css';

// export const URL = 'http://localhost:8080';
export const URL = 'http://localhost:9000';
export const GAME_ID = window.location.search.replace('?id=', '').replace('?id=', '');
console.log(window.location);

export function railsApi(apiName) {
  const func = (resolve, reject) => {
    const url = encodeURI(`${URL}/${apiName}`);
    console.log(`railsApi()...url=${url}`);
    request.get('http://localhost:9000/app/prelogin')
      .end((err, res) => {
        if (err) {
          reject(err);
        } else {
          console.log('railsApi()...res=', res);
          resolve(JSON.parse(res.text));
        }
      });
  };
  return new Promise(func);
}


function App() {

  useEffect(() => {
    console.log('useEffect !!!');
    // railsApi('api/sample/getGameInfo?id=' + GAME_ID)
    // "http://localhost:9000/app/prelogin"
    // railsApi('app/prelogin')
    //   .then((obj) => {
    //     console.log('SUCCESS! block data get!', obj);
    //   });

    request.get('http://localhost:9000/app/prelogin')
      .end((err, res) => {
        if (err) {
          // reject(err);
          console.error(err);
        } else {
          console.log('railsApi()...res=', res);
          var csrf = res.text;

          var data = {
            email: "kkamimura@example.com",
            pass: "iWKw06pvj",
          }

          request.post('http://localhost:9000/app/login')
            .set('_csrf', csrf)
            .send(data)
            .end((err, res) => {
              if (err) {
                // reject(err);
                console.error(err);
              } else {
                console.log('railsApi()...res=', res);
                var obj = JSON.parse(res.text);

                // resolve(JSON.parse(res.text));
              }
            });

          // resolve(JSON.parse(res.text));
        }
      });

    // アカウント作成
    /*
    var data = {
      username: 'aoyama',
      email: 'naoyama@digitalvision.co.jp',
      role: '1',
      password: 'aoyamanaoki'
    }

    request.post('http://localhost:9000/api/auth/signup').send(data)
    .end((err, res) => {
      if (err) {
        // reject(err);
        console.error(err);
      } else {
        console.log('railsApi()...res=', res);
        // resolve(JSON.parse(res.text));
      }
    });

    // ログイン
    var data = {
      username: 'aoyama',
      password: 'aoyamanaoki'
    }

    request.post('http://localhost:9000/api/auth/signin').send(data)
    .end((err, res) => {
      if (err) {
        // reject(err);
        console.error(err);
      } else {
        console.log('railsApi()...res=', res);
        // resolve(JSON.parse(res.text));
      }
    });
    */


  });
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          GAME = {GAME_ID}
        </a>
      </header>
    </div>
  );
}

export default App;
