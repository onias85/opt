/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Config.cpp
 * Author: marinap
 * 
 * Created on February 13, 2018, 10:09 AM
 */

#include "Config.hpp"

Config::Config() {
    // default values for opt configuration
    nStep       = 10;
    tol         = 1e-10;
    stepS       = 1.00E-3;
    stepL       = 1.20E-3;
    gradAllowed = 1.00E-3;
    maxSize     = 0.2;
}

Config::Config(const Config& orig) {
    nStep       = orig.nStep;
    tol         = orig.tol;
    stepS       = orig.stepS;
    stepL       = orig.stepL;
    gradAllowed = orig.gradAllowed;
    maxSize     = orig.maxSize;
}

void Config::print() {
    std::cout << "\tNSTEP = " << nStep << '\n';
    std::cout << "\tTOL   = " << tol   << '\n';
}

