//const {Client} = require('pg');
const {faker} = require('@faker-js/faker');
const cliProgress = require('cli-progress');
const fs = require('node:fs');

const output = '01_data.sql';

const stats = fs.statSync(output);

if (stats != undefined) {
    fs.unlinkSync(output);
}

/*
const client = new Client({
    user: 'bd2',
    host: 'localhost',
    database: 'bd2',
    password: 'passwordSicura',
    port: 5432
})
client.connect();

*/
const users = [];
const games = [];
const achievements = [];
const userAchivement = [];
const userGames = [];

new Promise(resolve => resolve()).then(async res => {
    fs.writeFileSync(output, 'DELETE FROM users;\n');
    fs.appendFileSync(output, 'DELETE FROM games;\n');
    fs.appendFileSync(output, 'DELETE FROM achievements;\n');
    fs.appendFileSync(output, 'DELETE FROM user_achievement;\n');
    fs.appendFileSync(output, 'DELETE FROM user_game;\n');
    //await client.query('DELETE FROM users');
    //await client.query('DELETE FROM games');
    //await client.query('DELETE FROM achievements');
    //await client.query('DELETE FROM user_achievement');
    //await client.query('DELETE FROM user_game');

    const NUMBER_OF_USERS = 150000;
    const NUMBER_OF_GAMES = 50000;

    const bar1 = new cliProgress.SingleBar({
        format: 'Users | {bar} | {percentage}% || {value}/{total} Rows || Eta: {eta}s',
        barCompleteChar: '\u2588',
        barIncompleteChar: '\u2591',
        hideCursor: true
    });
    bar1.start(NUMBER_OF_USERS, 0);
    const userUsernames = faker.helpers.uniqueArray(faker.internet.username, NUMBER_OF_USERS);
    for (let i = 0; i < NUMBER_OF_USERS; i++) {
        const user = {
            'username': userUsernames[i],
            'password': faker.internet.password(),
            'email': faker.internet.email(),
            'gender': faker.datatype.boolean(),
            'phone': faker.phone.number()
        }

        const dbValues = [user.username, user.password, user.email, user.gender, user.phone];

        users.push(user);
        //await client.query('INSERT INTO users(username, password, email, gender, phone) VALUES($1, $2, $3, $4, $5)', dbValues);
        fs.appendFileSync(output, `INSERT INTO users(username, password, email, gender, phone) VALUES('${user.username}', '${user.password}', '${user.email}', '${user.gender}', '${user.phone}');\n`);
        bar1.increment();
    }
    bar1.stop();

    const bar2 = new cliProgress.SingleBar({
        format: 'Games | {bar} | {percentage}% || {value}/{total} Rows || Eta: {eta}s',
        barCompleteChar: '\u2588',
        barIncompleteChar: '\u2591',
        hideCursor: true
    });
    bar2.start(NUMBER_OF_GAMES, 0);i
    const gameCodes = faker.helpers.uniqueArray(faker.string.uuid, NUMBER_OF_GAMES);
    for (let i = 0; i < NUMBER_OF_GAMES; i++) {
        const game = {
            'code': gameCodes[i],
            'name': faker.commerce.productName(),
            'description': faker.lorem.paragraphs(5),
            'price': faker.finance.amount(),
        }

        const dbValues = [game.code, game.name, game.description, game.price];

        games.push(game);

        //await client.query('INSERT INTO games(code, name, description, price) VALUES ($1, $2, $3, $4)', dbValues);
        fs.appendFileSync(output, `INSERT INTO games(code, name, description, price) VALUES ('${game.code}', '${game.name}', '${game.description}', '${game.price}');\n`);
        bar2.increment();
    }
    bar2.stop();

    const bar3 = new cliProgress.SingleBar({
        format: 'Achievements per Game | {bar} | {percentage}% || {value}/{total} Rows || Eta: {eta}s',
        barCompleteChar: '\u2588',
        barIncompleteChar: '\u2591',
        hideCursor: true
    });
    bar3.start(NUMBER_OF_GAMES, 0);
    for (let game of games) {
        for (let i = 0; i < faker.number.int({min: 0, max: 10}); i++) {
            const achievement = {
                name: faker.string.alphanumeric({length: 100}),
                description: faker.lorem.paragraphs(2),
                difficulty: faker.number.int({min: 1, max: 5}),
                game: game.code
            }

            const dbValues = [achievement.name, achievement.description, achievement.difficulty, achievement.game];

            achievements.push(achievement);

            //await client.query('INSERT INTO achievements(name, description, difficulty, game) VALUES($1, $2, $3, $4)', dbValues);
            fs.appendFileSync(output, `INSERT INTO achievements(name, description, difficulty, game) VALUES('${achievement.name}', '${achievement.description}', '${achievement.difficulty}', '${achievement.game}');\n`);
        }
        bar3.increment();
    }
    bar3.stop();

    const bar4 = new cliProgress.SingleBar({
        format: 'Games per User | {bar} | {percentage}% || {value}/{total} Rows || Eta: {eta}s',
        barCompleteChar: '\u2588',
        barIncompleteChar: '\u2591',
        hideCursor: true
    });
    bar4.start(NUMBER_OF_USERS, 0);
    for (let user of users) {
        const currentUserGames = []

        for (let i = 0; i < faker.number.int({min: 1, max: 50}); i++) {
            let gameFounded = undefined;
            while(true) {
                gameFounded = games[faker.number.int({max: games.length - 1})];
                if (currentUserGames.find(cug => cug.game == gameFounded.code) == undefined)
                    break;
            }
            const userGame = {
                user: user.username,
                game: gameFounded.code,
                purchase_date: faker.date.past().toISOString(),
                hours_played: faker.number.float({max: 450})
            }

            const dbValues = [userGame.user, userGame.game, userGame.purchase_date, userGame.hours_played];

            currentUserGames.push(userGame);

            //await client.query('INSERT INTO user_game("user", game, purchase_date, hours_played) VALUES($1, $2, $3, $4)', dbValues);
            fs.appendFileSync(output, `INSERT INTO user_game("user", game, purchase_date, hours_played) VALUES('${userGame.user}', '${userGame.game}', '${userGame.purchase_date}', '${userGame.hours_played}');\n`);
        }

        currentUserGames.forEach(cug => userGames.push(cug));
        bar4.increment();
    }
    bar4.stop();

    const bar5 = new cliProgress.SingleBar({
        format: 'Achievements per User | {bar} | {percentage}% || {value}/{total} Rows || Eta: {eta}s',
        barCompleteChar: '\u2588',
        barIncompleteChar: '\u2591',
        hideCursor: true
    });
    bar5.start(userGames.length, 0)
    for (let userGame of userGames) {
        const gameAchievements = achievements.filter(a => a.game === userGame.game);
        const max = gameAchievements.length - 1 > 0 ? gameAchievements.length - 1 : 0;
        for (var i = 0; i < faker.number.int({max}); i++) {
            const userAchievement = {
                user: userGame.user,
                achievement: gameAchievements[i].name,
                unlocked_date: faker.date.past().toISOString(),
                unlocked_at_played_hours: faker.number.float({max: userGame.hours_played})
            }

            const dbValues = [userAchievement.user, userAchievement.achievement, userAchievement.unlocked_date, userAchievement.unlocked_at_played_hours];

            //await client.query('INSERT INTO user_achievement("user", achievement, unlocked_date, unlocked_at_played_hours) VALUES($1,$2,$3,$4)', dbValues);
            fs.appendFileSync(output, `INSERT INTO user_achievement("user", achievement, unlocked_date, unlocked_at_played_hours) VALUES('${userAchievement.user}','${userAchievement.achievement}','${userAchievement.unlocked_date}','${userAchievement.unlocked_at_played_hours}');\n`);
        }

        bar5.increment();
    }
    bar5.stop();

    //client.end();
})


