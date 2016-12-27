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
		setColor(scaleColor(5,sumDefect,strategySize,0))
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
	
	def move() {
		def closest = minOneOf(other(players())) {
			distance(this)
		}
		if (closest != null)
			play(closest)
	}
	
	def evolve() {
		complexity++
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < strategySize; j++) {
				if (random.nextDouble() < mutationChance)
				{
					pickSides()
				}
				else {
					strategy << strategy[j]
					if (strategy[-1])
						sumDefect++
				}
			}
		}
		softEvolve()
		strategySize *= 4
	}
	
	def softEvolve() {
		for (int i = 0; i < strategySize; i++) {
			if (mutationChance / 2 > random.nextDouble()) {
				strategy[i] = !strategy[i]
				if (strategy[i])
					sumDefect++
				else
					sumDefect--
			}
		}
	}
	
	def kill() {
		if (lifespan <= 0)
			die()
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
		//label = lifespan.toString() + " "
		//label += sumDefect.toString() + " "
		//label += strategySize.toString() + "  "
		//label += strategy.toString()
		if (random.nextInt() % 5 == 0) {
			def closest = minOneOf(other(patches())) {
				count(playersHere())*maxDistance + distance(this)
			}
			if (count(closest.playersHere()) == 0)
			{
				def thisStrategy = strategy
				def thisStrategySize = strategySize
				def thisSumDefect = sumDefect
				def thisRandom = random
				def thisComplexity = complexity
				closest.sprout(1,{
					strategySize = thisStrategySize
					sumDefect = thisSumDefect
					random = thisRandom
					complexity = thisComplexity
					for (int i = 0; i < strategySize; i++)
						strategy << thisStrategy[i]
					if (complexity < maxComplexity)
						evolve()
					else
						softEvolve()
					initialize()
				}
					,Player)
			}
		}
		//label += sumDefect.toString() + " "
		//label += strategySize.toString() + "  "
		//label += strategy.toString()
		//println label
	}
}
