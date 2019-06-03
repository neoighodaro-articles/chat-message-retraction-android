const express = require('express');
const bodyParser = require('body-parser');
const Chatkit = require('@pusher/chatkit-server');
const app = express();

const chatkit = new Chatkit.default({
  instanceLocator: 'CHATKIT_INSTANCE_LOCATOR',
  key: 'CHATKIT_KEY'
});
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.post('/token', (req, res) => {
  const result = chatkit.authenticate({
    userId: req.query.user_id
  });
  res.status(result.status).send(result.body);
});

app.post('/delete-message', (req, res) => {
  const { messageId } = req.body;
  chatkit
    .deleteMessage({
      id: messageId
    })
    .then(() => console.log('deleted'))
    .catch(err => console.error(err));
  res.end();
});

const server = app.listen(3000, () => {
  console.log(`Express server running on port ${server.address().port}`);
});
