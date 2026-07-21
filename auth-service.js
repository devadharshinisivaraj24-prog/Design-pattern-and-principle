/**
 * JWT Authentication Service — single-file version
 *
 * Endpoints:
 *   POST /register   { email, password }              -> creates a user
 *   POST /login      { email, password }              -> returns a JWT
 *   GET  /me         Authorization: Bearer <token>     -> protected route
 *
 * Setup:
 *   npm install express jsonwebtoken bcryptjs
 *   node auth-service.js
 *
 * IMPORTANT: set a real secret via the JWT_SECRET env var in production:
 *   JWT_SECRET=$(node -e "console.log(require('crypto').randomBytes(64).toString('hex'))") node auth-service.js
 */

const express = require('express');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');

const app = express();
app.use(express.json());

const PORT = process.env.PORT || 3000;
const JWT_SECRET = process.env.JWT_SECRET || 'dev-only-secret-change-me';
const JWT_EXPIRES_IN = process.env.JWT_EXPIRES_IN || '1h';
const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// --- In-memory "database" (swap for a real DB in production) ---
const users = []; // { id, email, passwordHash }
let nextId = 1;

// --- Middleware: verify JWT on protected routes ---
function requireAuth(req, res, next) {
  const header = req.headers.authorization;
  if (!header || !header.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Missing or malformed Authorization header' });
  }

  const token = header.split(' ')[1];
  try {
    const payload = jwt.verify(token, JWT_SECRET);
    req.user = { id: payload.sub, email: payload.email };
    next();
  } catch (err) {
    const message = err.name === 'TokenExpiredError' ? 'Token expired' : 'Invalid token';
    return res.status(401).json({ error: message });
  }
}

// --- POST /register ---
app.post('/register', async (req, res) => {
  const { email, password } = req.body || {};

  if (!email || !password) {
    return res.status(400).json({ error: 'email and password are required' });
  }
  if (!EMAIL_RE.test(email)) {
    return res.status(400).json({ error: 'invalid email format' });
  }
  if (password.length < 8) {
    return res.status(400).json({ error: 'password must be at least 8 characters' });
  }
  if (users.some((u) => u.email === email)) {
    return res.status(409).json({ error: 'user already exists' });
  }

  const passwordHash = await bcrypt.hash(password, 12);
  const user = { id: nextId++, email, passwordHash };
  users.push(user);

  return res.status(201).json({ id: user.id, email: user.email });
});

// --- POST /login (returns the JWT) ---
app.post('/login', async (req, res) => {
  const { email, password } = req.body || {};

  if (!email || !password) {
    return res.status(400).json({ error: 'email and password are required' });
  }

  const user = users.find((u) => u.email === email);
  if (!user) {
    return res.status(401).json({ error: 'invalid credentials' });
  }

  const valid = await bcrypt.compare(password, user.passwordHash);
  if (!valid) {
    return res.status(401).json({ error: 'invalid credentials' });
  }

  const token = jwt.sign(
    { sub: user.id, email: user.email },
    JWT_SECRET,
    { expiresIn: JWT_EXPIRES_IN }
  );

  return res.json({ token, tokenType: 'Bearer', expiresIn: JWT_EXPIRES_IN });
});

// --- GET /me (protected example route) ---
app.get('/me', requireAuth, (req, res) => {
  return res.json({ user: req.user });
});

app.get('/health', (req, res) => res.json({ status: 'ok' }));

app.listen(PORT, () => {
  console.log(`Auth service listening on port ${PORT}`);
});
