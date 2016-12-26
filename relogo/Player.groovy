package darwinsdilemma.relogo

import static repast.simphony.relogo.Utility.*;

import static repast.simphony.relogo.UtilityG.*;

import darwinsdilemma.ReLogoTurtle
import repast.simphony.relogo.Plural;
import repast.simphony.relogo.Stop;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;
import repast.simphony.relogo.schedule.Go;
import repast.simphony.relogo.schedule.Setup;;

class Player extends ReLogoTurtle {
	def strategy = []
	def complexity = 0
	def strategySize = 1
	def lifespan = 12
	def history = 0
	def historySize = 0
	def sumDefect = 0
	def random
	
	def initialize() {
		random = new Random()
		setShape("circle")
		setColor(scaleColor(10,sumDefect,strategySize,0))
	}
	
	def pickSides() {
		random = new Random()
		if (random.nextInt() % 2 == 0) {
			strategy << true
			sumDefect++
		}
		else {
			strategy << false
		}
	}
	
	def step(maxDistance) {
		def closest = minOneOf(other(players())) {
			distance(this)
		}
		if (closest != null)
			play(closest)
		label = lifespan.toString() + " "
		label += history.toString() + " "
		label += strategy[history].toString()
		if (random.nextInt() % 5 == 0)
			reproduce(maxDistance)
	}
	
	def evolve() {
		complexity++
		strategySize *= 4
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < strategySize; j++) {
				strategy << strategy[j]
				if (strategy[-1])
					sumDefect++
			}
		}
	}
	
	def play(Player player) {
		def player1loss
		def player2loss
		if (strategy[history]&&player.strategy[player.history]) {
			lifespan -= 2
			player.lifespan -= 2
			save(true, true)
			player.save(true, true)
		}
		else if (!strategy[history]&&player.strategy[player.history]) {
			lifespan -= 3
			player.lifespan -= 0
			save(false, true)
			player.save(true, false)
		}
		else if (strategy[history]&&!player.strategy[player.history]) {
			lifespan -= 0
			player.lifespan -= 3
			save(true, false)
			player.save(false, true)
		}
		else {
			lifespan -= 1
			player.lifespan -= 1
			save(false, false)
			player.save(false, false)
		}
		if (player.lifespan <= 0)
			player.die()
		if (lifespan <= 0)
			die()
	}
	
	def save(boolean me,boolean you) {
		history *= 4
		if (me)
			history += 2
		if (you)
			history += 1
		history = history % strategySize
	}
	
	def reproduce(maxDistance) {
		def closest = minOneOf(other(patches())) {
			count(playersHere())*maxDistance + distance(this)
		}
		closest.sprout(1,{
			strategy = this.strategy
			strategySize = this.strategySize
			sumDefect = this.sumDefect
			initialize()
		}
			,Player)
	}
}
